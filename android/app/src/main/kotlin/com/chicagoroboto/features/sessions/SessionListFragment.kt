package com.chicagoroboto.features.sessions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.chicagoroboto.R
import com.chicagoroboto.databinding.GenericListBinding
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.ext.guard
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.sessions.SessionListPresenter.Event.SetSessionDate
import com.chicagoroboto.features.sessions.SessionListPresenter.Model.Session
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import timber.log.error
import javax.inject.Inject
import javax.inject.Provider

class SessionListFragment : Fragment(R.layout.generic_list) {

  companion object {
    private const val ARG_DATE = "date"
    fun create(date: String) = SessionListFragment().apply {
      arguments = bundleOf(ARG_DATE to date)
    }
  }

  private val presentation: Presentation<SessionListPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  @Inject lateinit var presenterProvider: Provider<SessionListPresenter>
  @Inject lateinit var sessionNavigator: SessionNavigator

  private lateinit var binding: GenericListBinding
  private lateinit var adapter: SessionAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = GenericListBinding.bind(view)
    binding.list.apply {
      layoutManager = LinearLayoutManager(context, VERTICAL, false)
      addItemDecoration(SessionItemDecoration(context))
      this@SessionListFragment.adapter = SessionAdapter(layoutInflater, object : SessionAdapter.Callback {
        override fun onSessionClicked(session: Session) {
          sessionNavigator.showSession(session.session)
        }
      }).also { adapter = it }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    context!!.getComponent<MainComponent>().sessionListComponent().inject(this)

    val sessionDate = arguments?.getString(ARG_DATE) guard {
      Timber.error { "Missing required fragment argument: date" }
      return
    }
    presentation.presenter.events.offer(SetSessionDate(sessionDate))

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect {
        adapter.submitList(it.sessions)
        if (it.currentSessionIndex != -1) {
          binding.list.scrollToPosition(it.currentSessionIndex)
        }
      }
    }
  }
}

