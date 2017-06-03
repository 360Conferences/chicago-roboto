package com.conferences.features.sessions

import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.conferences.R
import com.conferences.ext.getComponent
import com.conferences.features.main.MainComponent
import kotlinx.android.synthetic.main.view_sessions.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SessionDateView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle), SessionDateListMvp.View {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    @Inject lateinit var presenter: SessionDateListMvp.Presenter

    private val adapter: SessionPagerAdapter

    init {
        context.getComponent<MainComponent>().sessionListComponent().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_sessions, this, true)

        adapter = SessionPagerAdapter()
        pager.adapter = adapter

        tabs.setupWithViewPager(pager)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    override fun showNoSessionDates() {
        adapter.dates.clear()
        adapter.notifyDataSetChanged()
    }

    override fun showSessionDates(sessionDates: List<String>) {
        adapter.dates.clear()
        adapter.dates.addAll(sessionDates)
        adapter.notifyDataSetChanged()

        if (sessionDates.size > 1) {
            tabs.visibility = View.VISIBLE
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