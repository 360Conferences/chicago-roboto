package com.chicagoroboto.features.sessiondetail

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.chicagoroboto.R
import com.chicagoroboto.data.AvatarProvider
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.sessiondetail.feedback.FeedbackDialog
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.view_location.view.*
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
        val activity = context as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity.finish()
        }

        val startTime = DateUtils.formatDateTime(context, session.startTime?.time ?: 0,
            DateUtils.FORMAT_SHOW_TIME)
        val endTime = DateUtils.formatDateTime(context, session.endTime?.time ?: 0,
            DateUtils.FORMAT_SHOW_TIME)
        banner.text = String.format(context.getString(R.string.session_detail_time), session.location,
            startTime, endTime)

        description.text = session.description

        session.address?.let { address ->
            location.visibility = View.VISIBLE
            location.text = address

            location.setOnClickListener {
                val mapUri = Uri.parse("geo:0,0?q=${session.location},+${address.replace(Regex("[\\r\\n\\s]"), "+")}")
                val intent = Intent(Intent.ACTION_VIEW, mapUri)
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)
            }
        } ?: run {
            location.visibility = GONE
        }

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
