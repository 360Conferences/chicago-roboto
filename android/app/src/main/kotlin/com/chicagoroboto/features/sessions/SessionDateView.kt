package com.chicagoroboto.features.sessions

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.TabHolder
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.main.MainView
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SessionDateView : Fragment(), SessionDateListMvp.View, MainView {

    override val titleResId = R.string.action_schedule

    @Inject lateinit var presenter: SessionDateListMvp.Presenter

    private var tabLayout: TabLayout? = null
        set(value) {
            field = value
            field?.setupWithViewPager(pager)
        }

    private lateinit var pager: ViewPager

    private val adapter = SessionPagerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.view_sessions, container, false).apply {
        pager = findViewById(R.id.pager)
        pager.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().getComponent<MainComponent>().sessionListComponent().inject(this)

        presenter.onAttach(this)

        when (val parentContext = context) {
            is TabHolder -> {
                tabLayout = parentContext.tabLayout
            }
        }
    }

    override fun onDestroy() {
        tabLayout?.visibility = View.GONE
        tabLayout = null
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showNoSessionDates() {
        adapter.dates.clear()
        adapter.notifyDataSetChanged()
    }

    override fun showSessionDates(sessionDates: List<String>) {
        adapter.dates.clear()
        adapter.dates.addAll(sessionDates)
        adapter.notifyDataSetChanged()

        if (sessionDates.isNotEmpty()) {
            tabLayout?.visibility = View.VISIBLE
        }
    }

    override fun scrollToCurrentDay() {
        if (adapter.dates.isNotEmpty()) {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val index = adapter.dates.indexOfFirst { DateUtils.isToday(format.parse(it).time) }
            if (index >= 0) {
                pager.setCurrentItem(index, false)
            }
        }
    }
}