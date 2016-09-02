package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getComponent
import com.gdgchicagowest.windycitydevcon.model.Session
import com.gdgchicagowest.windycitydevcon.model.Speaker
import kotlinx.android.synthetic.main.view_session_detail.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SessionDetailView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        CoordinatorLayout(context, attrs, defStyle), SessionDetailMvp.View {

    private val format = SimpleDateFormat("hh:mma")

    @Inject lateinit var presenter: SessionDetailMvp.Presenter

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        context.getComponent<SessionDetailComponent>().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_session_detail, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    fun setSession(sessionId: String) {
       presenter.setSessionId(sessionId)
    }

    override fun showSessionDetail(session: Session) {
        toolbar.title = session.name
        banner.text = "${session.room}, ${format.format(session.startTime)}-${format.format(session.endTime)}"
        description.text = session.description

        val now = Date()
        if (now.before(session.startTime)) {
            status.visibility = GONE
        } else {
            status.visibility = VISIBLE

            val statusString = if (now.before(session.endTime)) {
                R.string.status_in_progress
            } else {
                R.string.status_over
            }
            status.setText(statusString)
        }
    }

    override fun showSpeakerInfo(speakers: Set<Speaker>) {
        // TODO
    }
}