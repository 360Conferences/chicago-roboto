package com.chicagoroboto.features.speakerdetail

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.chicagoroboto.R
import com.chicagoroboto.ext.getActivity
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils
import kotlinx.android.synthetic.main.view_speaker_detail.view.bio
import kotlinx.android.synthetic.main.view_speaker_detail.view.github
import kotlinx.android.synthetic.main.view_speaker_detail.view.image
import kotlinx.android.synthetic.main.view_speaker_detail.view.name
import kotlinx.android.synthetic.main.view_speaker_detail.view.toolbar
import kotlinx.android.synthetic.main.view_speaker_detail.view.twitter
import javax.inject.Inject


class SpeakerDetailView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), SpeakerDetailMvp.View {

    @Inject lateinit var presenter: SpeakerDetailMvp.Presenter

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        context.getComponent<SpeakerDetailComponent>().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_speaker_detail, this, true)

        var speakerId = ""
        getActivity()?.let {
            speakerId = it.intent.getStringExtra("speaker_id")
        }

        ViewCompat.setTransitionName(image, "image_$speakerId")
        toolbar.setNavigationOnClickListener {
            if (context is Activity) {
                ActivityCompat.finishAfterTransition(context)
            }
        }

        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    fun setSpeakerId(speakerId: String) {
        presenter.setSpeakerId(speakerId)
    }

    override fun showSpeaker(speaker: Speaker) {
        name.text = speaker.name
        bio.text = speaker.bio

        if (speaker.twitter != null && speaker.twitter!!.isNotEmpty()) {
            twitter.visibility = VISIBLE
            twitter.text = speaker.twitter
            twitter.setCompoundDrawablesWithIntrinsicBounds(
                DrawableUtils.create(context, R.drawable.ic_logo_twitter),
                null,
                null,
                null)
        } else {
            twitter.visibility = GONE
        }

        if (speaker.github?.isNotEmpty() ?: false) {
            github.visibility = VISIBLE
            github.text = speaker.github
            github.setCompoundDrawablesWithIntrinsicBounds(
                DrawableUtils.create(context, R.drawable.ic_logo_github),
                null,
                null,
                null)

        }
        else {
            github.visibility = GONE
        }

        Glide.with(context)
                .load(speaker.avatar)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .into(object : BitmapImageViewTarget(image) {
                    override fun setResource(resource: Bitmap?) {
                        image.setImageBitmap(resource)
                        ActivityCompat.startPostponedEnterTransition(getActivity())
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        ActivityCompat.startPostponedEnterTransition(getActivity())
                    }
                })
    }

}