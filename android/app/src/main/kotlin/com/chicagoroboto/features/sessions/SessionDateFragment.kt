package com.chicagoroboto.features.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.chicagoroboto.R
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
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

  private lateinit var toolbar: Toolbar
  private lateinit var tabs: TabLayout
  private lateinit var pager: ViewPager2

  private lateinit var adapter: SessionPagerAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.view_sessions, container,false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    pager = view.findViewById(R.id.pager)
    pager.adapter = adapter

    toolbar = view.findViewById(R.id.toolbar)
    tabs = view.findViewById(R.id.tabs)
    adapter.mediateTabs(tabs, pager)

    val appBar: AppBarLayout = view.findViewById(R.id.app_bar)
    val initialToolbarPadding = appBar.paddingTop
    appBar.setOnApplyWindowInsetsListener { view, insets ->
      view.updatePadding(
          top = insets.systemWindowInsetTop + initialToolbarPadding
      )
      insets
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
            pager.setCurrentItem(index, false)
          }
        }
      }
    }
  }
}
