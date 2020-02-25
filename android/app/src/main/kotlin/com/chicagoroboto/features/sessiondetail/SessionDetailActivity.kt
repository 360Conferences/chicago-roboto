package com.chicagoroboto.features.sessiondetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.databinding.ActivitySessionDetailBinding
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.ext.guard
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Event.SetSessionId
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Event.ToggleFavorite
import com.chicagoroboto.features.sessiondetail.SessionDetailPresenter.Model
import com.chicagoroboto.features.sessiondetail.feedback.FeedbackDialog
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import com.chicagoroboto.features.speakerdetail.SpeakerDetailActivity
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.model.Speaker
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class SessionDetailActivity : AppCompatActivity(), SpeakerNavigator {

  @Inject lateinit var presenterProvider: Provider<SessionDetailPresenter>

  private lateinit var component: SessionDetailComponent
  private lateinit var binding: ActivitySessionDetailBinding

  private val presentation: Presentation<SessionDetailPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  private val speakerAdapter = SpeakerAdapter(true, object : SpeakerAdapter.Callback {
    override fun onSpeakerClicked(speaker: Speaker) {
      navigateToSpeaker(speaker.id)
    }
  })

  @InternalCoroutinesApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component = getAppComponent().sessionDetailComponentFactory.create(this)
    component.inject(this)

    val sessionId = intent.getStringExtra("session_id") guard {
      Log.e("SessionDetailActivity", "Missing required string extra: session_id")
      finish()
      return
    }

    val presenter = presentation.presenter

    binding = ActivitySessionDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.toolbar.setNavigationOnClickListener { finish() }

    val speakers: RecyclerView = findViewById(R.id.speakers)
    speakers.adapter = speakerAdapter
    speakers.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)

    // initially hide the feedback button until we get a session
    (binding.feedback as View).visibility = CoordinatorLayout.GONE
    binding.feedback.setOnClickListener { FeedbackDialog(this, sessionId).show() }
    binding.favorite.setOnClickListener { presenter.events.offer(ToggleFavorite) }

    lifecycleScope.launchWhenStarted {
      presenter.models.collect { bindModel(it) }
    }

    presenter.events.offer(SetSessionId(sessionId))
  }

  private fun bindModel(model: Model) {
    // Session
    binding.toolbar.title = model.session.title
    binding.banner.text = getString(
        R.string.session_detail_time,
        model.session.location,
        DateUtils.formatDateTime(this, model.session.startTime?.time
            ?: 0, DateUtils.FORMAT_SHOW_TIME),
        DateUtils.formatDateTime(this, model.session.endTime?.time
            ?: 0, DateUtils.FORMAT_SHOW_TIME)
    )
    binding.description.text = model.session.description

    val address = model.session.address
    if (address.isNotBlank()) {
      binding.location.visibility = View.VISIBLE
      binding.location.text = address

      binding.location.setOnClickListener {
        val mapUri = Uri.parse("geo:0,0?q=${model.session.location},+${address.replace(Regex("[\\r\\n\\s]"), "+")}")
        val intent = Intent(Intent.ACTION_VIEW, mapUri).apply {
          setPackage("com.google.android.apps.maps")
        }
        startActivity(intent)
      }
    } else {
      binding.location.visibility = View.GONE
    }

    val now = Date()
    if (model.session.startTime?.after(now) == true) {
      binding.status.visibility = View.GONE
      binding.favorite.show()
    } else {
      binding.status.visibility = View.VISIBLE
      binding.favorite.hide()

      if (model.session.endTime?.after(now) == true) {
        binding.status.setText(R.string.status_in_progress)
      } else {
        binding.status.setText(R.string.status_over)
        binding.feedback.show()
      }
    }

    speakerAdapter.submitList(model.speakers)

    binding.favorite.setImageResource(
        if (model.isFavorite) R.drawable.ic_favorite_white_24dp
        else R.drawable.ic_favorite_border_white_24dp
    )
  }

  override fun navigateToSpeaker(id: String, image: View?) {
    SpeakerDetailActivity.navigate(this, id, image)
  }

}
