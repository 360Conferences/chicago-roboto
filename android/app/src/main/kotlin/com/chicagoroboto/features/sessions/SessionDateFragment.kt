package com.chicagoroboto.features.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chicagoroboto.R
import com.chicagoroboto.databinding.SessionsBinding
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class SessionDateFragment : Fragment() {

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

  private val presentation: Presentation<SessionDatePresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  @Inject lateinit var presenterProvider: Provider<SessionDatePresenter>

  private lateinit var binding: SessionsBinding
  private lateinit var adapter: SessionPagerAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = SessionsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.pager.adapter = adapter
    adapter.mediateTabs(binding.tabs, binding.pager)

    binding.appBar.doOnApplyWindowInsets { view, insets, initialState ->
      view.updatePadding(
          top = initialState.paddings.top + insets.systemWindowInsetTop
      )
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    context!!.getComponent<MainComponent>().sessionListComponent().inject(this)

    adapter = SessionPagerAdapter(this)

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect {
        adapter.dates.clear()
        adapter.dates.addAll(it.dates)
        adapter.notifyDataSetChanged()

        // Set the current tab to today
        if (it.dates.isNotEmpty()) {
          val today = dateFormat.format(Date())
          val index = adapter.dates.indexOfFirst { it.id == today }
          if (index >= 0) {
            binding.pager.setCurrentItem(index, false)
          }
        }
      }
    }
  }
}
