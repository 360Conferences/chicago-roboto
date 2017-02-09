package com.chicagoroboto.features.speakerdetail

import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.chicagoroboto.R
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.model.Speaker
import kotlinx.android.synthetic.main.view_speaker_detail.view.*
import javax.inject.Inject

class SpeakerDetailView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ConstraintLayout(context, attrs, defStyle), SpeakerDetailMvp.View {

    @Inject lateinit var presenter: SpeakerDetailMvp.Presenter

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        context.getComponent<SpeakerDetailComponent>().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_speaker_detail, this, true)
        ViewCompat.setTransitionName(image, "image")
        toolbar.setNavigationOnClickListener {
            if (context is Activity) {
                context.finish()
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
        } else {
            twitter.visibility = GONE
        }

        Glide.with(context)
                .load(speaker.avatar)
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .into(image)
    }

}