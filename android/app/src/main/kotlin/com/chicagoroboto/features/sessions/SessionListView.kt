package com.chicagoroboto.features.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.chicagoroboto.R
import com.chicagoroboto.ext.asObservable
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainComponent
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

private const val ARG_DATE = "date"

fun makeSessionListView(date: String) = SessionListView().apply {
  arguments = bundleOf(
      ARG_DATE to date
  )
}

class SessionListView : Fragment() {

  private lateinit var list: RecyclerView
  private lateinit var adapter: SessionAdapter

  @FlowPreview
  @InternalCoroutinesApi
  @ExperimentalCoroutinesApi
  @Inject lateinit var viewModel: SessionListViewModel

  @Inject lateinit var sessionNavigator: SessionNavigator

  private class SimpleDiffCallback(val old: List<Any>, val new: List<Any>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      old[oldItemPosition] == new[newItemPosition]

    override fun getOldListSize(): Int = old.size
    override fun getNewListSize(): Int = new.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      old[oldItemPosition] == new[newItemPosition]
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.view_session_list, container, false).apply {
    list = findViewById(R.id.list)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    list.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
    list.addItemDecoration(SessionItemDecoration(requireContext()))

    adapter = SessionAdapter { id ->
      sessionNavigator.showSession(id)
    }
    list.adapter = adapter
  }

  @FlowPreview
  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    requireContext().getComponent<MainComponent>().sessionListComponent().inject(this)

    viewModel.viewState
        .asObservable()
        .autoDisposable(AndroidLifecycleScopeProvider.from(this))
        .subscribe { state ->
          // Update the session list
          val diffResult = DiffUtil.calculateDiff(
              SimpleDiffCallback(adapter.sessions, state?.sessions ?: emptyList())
          )
          adapter.sessions.clear()
          state?.let { adapter.sessions.addAll(it.sessions) }
          diffResult.dispatchUpdatesTo(adapter)

          // TODO handle empty list?
        }
    viewModel.viewEvents
        .asObservable()
        .autoDisposable(AndroidLifecycleScopeProvider.from(this))
        .subscribe { event ->
          when (event) {
            is ScrollToSessionIndex -> list.scrollToPosition(event.index)
            null -> {} // no op
          }
        }

    val date = arguments?.getString(ARG_DATE)
        ?: throw IllegalStateException("Missing date argument")
    viewModel.setDate(date)
  }
}

