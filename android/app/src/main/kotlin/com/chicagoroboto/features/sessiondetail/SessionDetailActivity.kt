package com.chicagoroboto.features.sessiondetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.ext.guard
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Event.SetSessionId
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Event.ToggleFavorite
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Model
import com.chicagoroboto.features.sessiondetail.feedback.FeedbackDialog
import com.chicagoroboto.features.speakerdetail.SpeakerDetailActivity
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.utils.DrawableUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SessionDetailActivity : AppCompatActivity(), SpeakerNavigator {
  private val scope = MainScope()

  private lateinit var component: SessionDetailComponent

  @Inject lateinit var presenter: SessionDetailPresenter

  private lateinit var speakerAdapter: SpeakerAdapter

  private lateinit var toolbar: Toolbar
  private lateinit var banner: TextView
  private lateinit var description: TextView
  private lateinit var location: TextView
  private lateinit var status: TextView
  private lateinit var favorite: FloatingActionButton
  private lateinit var feedback: FloatingActionButton

  @InternalCoroutinesApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component = getAppComponent().sessionDetailComponentFactory.create(this).also { it.inject(this) }

    val sessionId = intent.getStringExtra("session_id") guard {
      Log.e("SessionDetailActivity", "Missing required string extra: session_id")
      finish()
      return
    }

    setContentView(R.layout.activity_session_detail)
    toolbar = findViewById(R.id.toolbar)
    banner = findViewById(R.id.banner)
    description = findViewById(R.id.description)
    location = findViewById(R.id.location)
    status = findViewById(R.id.status)
    favorite = findViewById(R.id.favorite)
    feedback = findViewById(R.id.feedback)

    toolbar.setNavigationOnClickListener { finish() }

    speakerAdapter = SpeakerAdapter(true, object : SpeakerAdapter.Callback {
      override fun onSpeakerClicked(speaker: Model.Speaker) {
        navigateToSpeaker(speaker.id)
      }
    })
    val speakers: RecyclerView = findViewById(R.id.speakers)
    speakers.adapter = speakerAdapter
    speakers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    // initially hide the feedback button until we get a session
    (feedback as View).visibility = CoordinatorLayout.GONE
    feedback.setOnClickListener { FeedbackDialog(this, sessionId).show() }
    favorite.setOnClickListener { presenter.onEvent(ToggleFavorite) }

    scope.launch {
      presenter.models.collect { bindModel(it) }
    }

    scope.launch {
      presenter.start()
    }

    presenter.onEvent(SetSessionId(sessionId))
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }

  private fun bindModel(model: Model) {
    // Session
    toolbar.title = model.session.title
    banner.text = getString(
        R.string.session_detail_time,
        model.session.location,
        DateUtils.formatDateTime(this, model.session.startTime?.time
            ?: 0, DateUtils.FORMAT_SHOW_TIME),
        DateUtils.formatDateTime(this, model.session.endTime?.time
            ?: 0, DateUtils.FORMAT_SHOW_TIME)
    )
    description.text = model.session.description

    val address = model.session.address
    if (address != null) {
      location.visibility = View.VISIBLE
      location.text = address

      location.setOnClickListener {
        val mapUri = Uri.parse("geo:0,0?q=${location},+${address.replace(Regex("[\\r\\n\\s]"), "+")}")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
      }
    } else {
      location.visibility = View.GONE
    }

    val now = Date()
    if (model.session.startTime?.after(now) == true) {
      status.visibility = View.GONE
      favorite.show()
    } else {
      status.visibility = View.VISIBLE
      favorite.hide()

      if (model.session.endTime?.after(now) == true) {
        status.setText(R.string.status_in_progress)
      } else {
        status.setText(R.string.status_over)
        feedback.show()
      }
    }

    speakerAdapter.submitList(model.speakers)

    favorite.setImageResource(
        if (model.isFavorite) R.drawable.ic_favorite_white_24dp
        else R.drawable.ic_favorite_border_white_24dp
    )
  }

  override fun navigateToSpeaker(id: String, image: View?) {
    SpeakerDetailActivity.navigate(this, id, image)
  }

}
