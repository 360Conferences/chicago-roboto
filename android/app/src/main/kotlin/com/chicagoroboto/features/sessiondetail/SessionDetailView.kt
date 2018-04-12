package com.chicagoroboto.features.sessiondetail

import android.app.Activity
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import com.chicagoroboto.R
import com.chicagoroboto.data.AvatarProvider
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.sessiondetail.feedback.FeedbackDialog
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils
import com.chicagoroboto.utils.getFormattedSessionTime
import kotlinx.android.synthetic.main.view_session_detail.view.*
import java.util.Date
import javax.inject.Inject

class SessionDetailView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        CoordinatorLayout(context, attrs, defStyle), SessionDetailMvp.View {

    @Inject lateinit var speakerNavigator: SpeakerNavigator
    @Inject lateinit var presenter: SessionDetailMvp.Presenter
    @Inject lateinit var avatarProvider: AvatarProvider

    private val speakerAdapter: SpeakerAdapter
    private var sessionId: String? = null

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        context.getComponent<SessionDetailComponent>().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_session_detail, this, true)
        toolbar.setNavigationOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }

        speakerAdapter = SpeakerAdapter(avatarProvider, true, { speaker, image ->
            speakerNavigator.navigateToSpeaker(speaker.id!!, image)
        })
        speakers.adapter = speakerAdapter
        speakers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // initially hide the feedback button until we get a session
        feedback.visibility = GONE
        feedback.setOnClickListener {
            FeedbackDialog(context, sessionId!!).show()
        }
        favorite.setOnClickListener {
            presenter.toggleFavorite()
        }

        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    fun setSession(sessionId: String) {
        this.sessionId = sessionId
        presenter.setSessionId(sessionId)
    }

    override fun showSessionDetail(session: Session) {
        toolbar.title = session.title

        val startTime = getFormattedSessionTime(session.startTime)
        val endTime = getFormattedSessionTime(session.endTime)
        banner.text = String.format(context.getString(R.string.session_detail_time), session.room,
            startTime, endTime)

        description.text = session.abstract

        val now = Date()
        if (now.before(session.startTime)) {
            status.visibility = GONE
            favorite.visibility = VISIBLE
        } else {
            status.visibility = VISIBLE
            favorite.visibility = GONE

            val statusString = if (now.before(session.endTime)) {
                R.string.status_in_progress
            } else {
                R.string.status_over
            }
            status.setText(statusString)

            feedback.visibility = VISIBLE
        }
    }

    override fun showSpeakerInfo(speakers: Set<Speaker>) {
        speakerAdapter.speakers.clear()
        speakerAdapter.speakers.addAll(speakers)
        speakerAdapter.notifyDataSetChanged()
    }

    override fun setIsFavorite(isFavorite: Boolean) {
        val drawable = if (isFavorite) {
            DrawableUtils.create(context, R.drawable.ic_favorite_white_24dp)
        } else {
            DrawableUtils.create(context, R.drawable.ic_favorite_border_white_24dp)
        }
        favorite.setImageDrawable(drawable)
    }
}
