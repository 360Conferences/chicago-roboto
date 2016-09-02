package com.gdgchicagowest.windycitydevcon.features.sessions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getComponent
import com.gdgchicagowest.windycitydevcon.features.dates.SessionDateListMvp
import kotlinx.android.synthetic.main.view_sessions.view.*
import javax.inject.Inject

class SessionDateView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle), SessionDateListMvp.View {

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    @Inject lateinit var presenter: SessionDateListMvp.Presenter

    private val adapter: SessionPagerAdapter

    init {
        context.getComponent<SessionListComponent>().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_sessions, this, true)

        toolbar.setTitle(R.string.app_name)

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

        if (sessionDates.size > 1) {
            tabs.visibility = View.VISIBLE
        }
    }
}