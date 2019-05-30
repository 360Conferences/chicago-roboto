package com.chicagoroboto.features.sessions

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import javax.inject.Inject

class SessionListView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    RecyclerView(context, attrs, defStyle) {

  private lateinit var adapter: SessionAdapter

  @Inject lateinit var viewModel: SessionListViewModel
  @Inject lateinit var sessionNavigator: SessionNavigator

  private val viewStateObservable = { state: SessionListViewState ->
    // Update the session list
    val diffResult = DiffUtil.calculateDiff(SimpleDiffCallback(adapter.sessions, state.sessions))
    adapter.sessions.clear()
    adapter.sessions.addAll(state.sessions)
    diffResult.dispatchUpdatesTo(adapter)

    // TODO handle empty list?
  }

  private val viewEventObservable = { event: SessionListViewEvent ->
    when (event) {
      is ScrollToSessionIndex -> scrollToPosition(event.index)
    }
  }

  private class SimpleDiffCallback(val old: List<Any>, val new: List<Any>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      old[oldItemPosition] == new[newItemPosition]

    override fun getOldListSize(): Int = old.size
    override fun getNewListSize(): Int = new.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      old[oldItemPosition] == new[newItemPosition]
  }

  init {
    context.getComponent<MainComponent>().sessionListComponent().inject(this)

    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
    )
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    addItemDecoration(SessionItemDecoration(context))
    adapter = SessionAdapter { id ->
      sessionNavigator.showSession(id)
    }
    super.setAdapter(adapter)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    viewModel.viewState.observe(viewStateObservable)
    viewModel.viewEvents.observe(viewEventObservable)
  }

  override fun onDetachedFromWindow() {
    viewModel.viewState.removeObserver(viewStateObservable)
    viewModel.viewEvents.removeObserver(viewEventObservable)
    super.onDetachedFromWindow()
  }

  fun setDate(date: String) {
    viewModel.setDate(date)
  }
}

