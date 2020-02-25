package com.chicagoroboto.features.speakerdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chicagoroboto.R
import com.chicagoroboto.databinding.SpeakerDetailBinding
import com.chicagoroboto.ext.getActivity
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.ext.guard
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import timber.log.error
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class SpeakerDetailActivity : AppCompatActivity() {

  companion object {
    @JvmStatic fun navigate(activity: Activity, speakerId: String, image: View? = null) {
      val intent = Intent(activity, SpeakerDetailActivity::class.java)
      intent.putExtra("speaker_id", speakerId)

      // FIXME: the shared image is transition properly. The start/end locations are off
//            if (image != null) {
//                ViewCompat.setTransitionName(image, "image_$speakerId")
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
//                    *buildTransitionPairs(image))
//                ActivityCompat.startActivity(activity, intent, options.toBundle())
//            } else {
      activity.startActivity(intent)
//            }
    }

    @SuppressLint("NewApi") @JvmStatic
    private fun buildTransitionPairs(view: View): Array<Pair<View, String>> {
      val pairs = ArrayList<Pair<View, String>>()

      view.getActivity()?.let {
        val decor = it.window.decorView

        val statusBarBackground: View = decor.findViewById(android.R.id.statusBarBackground)
        if (statusBarBackground != null) {
          pairs.add(Pair.create(statusBarBackground, statusBarBackground.transitionName))
        }

        val navBar: View = decor.findViewById(android.R.id.navigationBarBackground)
        if (navBar != null) {
          pairs.add(Pair.create(navBar, navBar.transitionName))
        }

        pairs.add(Pair.create(view, view.transitionName))

        return pairs.toTypedArray()
      }

      return pairs.toTypedArray()
    }
  }

  @Inject lateinit var presenterProvider: Provider<SpeakerDetailPresenter>

  private lateinit var binding: SpeakerDetailBinding
  private val presentation: Presentation<SpeakerDetailPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerSpeakerDetailComponent.factory()
        .create(getAppComponent())
        .inject(this)

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      postponeEnterTransition()
    }

    val speakerId = intent.getStringExtra("speaker_id") guard {
      Timber.error { "Missing required extra: speaker_id" }
      finish()
      return
    }

    binding = SpeakerDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.toolbar.setNavigationOnClickListener {
      onBackPressed()
    }

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect { model ->
        binding.name.text = model.speaker.name
        binding.bio.text = model.speaker.bio

        if (model.speaker.twitter.isNotBlank()) {
          binding.twitter.visibility = View.VISIBLE
          binding.twitter.text = model.speaker.twitter
          binding.twitter.setOnClickListener {
            startActivity(
                Intent(ACTION_VIEW, "https://www.twitter.com/${model.speaker.twitter.removePrefix("@")}".toUri())
            )
          }
        } else {
          binding.twitter.visibility = View.GONE
        }

        if (model.speaker.github.isNotBlank()) {
          binding.github.visibility = View.VISIBLE
          binding.github.text = model.speaker.github
          binding.github.setOnClickListener {
            startActivity(
                Intent(ACTION_VIEW, "https://www.github.com/${model.speaker.github}".toUri())
            )
          }
        } else {
          binding.github.visibility = View.GONE
        }

        Glide.with(this@SpeakerDetailActivity)
            .load(model.speaker.avatarUrl)
            .centerCrop()
            .dontAnimate()
            .listener(object : RequestListener<String, GlideDrawable> {
              override fun onResourceReady(drawable: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
              }

              override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
              }
            })
            .into(binding.image)
      }
    }

    presentation.presenter.events.offer(SpeakerDetailPresenter.Event.SetSpeakerId(speakerId))
  }
}
