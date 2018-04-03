package com.chicagoroboto.features.sessiondetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chicagoroboto.R
import com.chicagoroboto.data.AvatarProvider
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils
import kotlinx.android.synthetic.main.item_speaker.view.*

internal class SpeakerAdapter(private val avatarProvider: AvatarProvider,
                              val wrapsWidth: Boolean = true,
                              val onSpeakerClickedListener: ((speaker: Speaker, view: View) -> Unit)) :
        RecyclerView.Adapter<SpeakerAdapter.ViewHolder>() {

    val speakers: MutableList<Speaker> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_speaker, parent, false)
        if (!wrapsWidth) {
            v.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return ViewHolder(v, avatarProvider, onSpeakerClickedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(speakers[position])
    }

    override fun getItemCount(): Int {
        return speakers.size
    }

    internal class ViewHolder(itemView: View,
                              private val avatarProvider: AvatarProvider,
                              private val onSpeakerClickedListener: ((speaker: Speaker, view: View) -> Unit)) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var speaker: Speaker? = null
        val image: ImageView
        val name: TextView
        val title: TextView

        init {
            image = itemView.image
            name = itemView.name
            title = itemView.title
            itemView.setOnClickListener(this)
        }

        fun bind(speaker: Speaker) {
            this.speaker = speaker
            name.text = speaker.name

            avatarProvider.getAvatarUri(speaker) {
                Glide.with(itemView.context)
                    .load(it)
                    .asBitmap()
                    .placeholder(DrawableUtils.create(itemView.context, R.drawable.ph_speaker))
                    .into(image)
            }
        }

        override fun onClick(v: View?) {
            val sp = speaker
            if (sp != null) {
                onSpeakerClickedListener(sp, image)
            }
        }
    }
}