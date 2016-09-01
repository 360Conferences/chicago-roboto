package com.gdgchicagowest.windycitydevcon.features.dates

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.data.FirebaseSessionDateProvider
import kotlinx.android.synthetic.main.view_sessions.view.*

class SessionDateView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
        FrameLayout(context, attrs, defStyleAttr, defStyleRes), SessionDateListMvp.View {

    private val presenter: SessionDatePresenter
    private val adapter: SessionPagerAdapter

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, null, 0)
    constructor(context: Context?) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sessions, this, true)

        presenter = SessionDatePresenter(FirebaseSessionDateProvider())

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
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSessionDates(sessionDates: List<String>) {
        adapter.dates.clear()
        adapter.dates.addAll(sessionDates)
        adapter.notifyDataSetChanged()
    }
}