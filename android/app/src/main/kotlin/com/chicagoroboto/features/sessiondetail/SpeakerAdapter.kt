package com.chicagoroboto.features.sessiondetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chicagoroboto.R
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils

internal class SpeakerAdapter(
    private val wrapsWidth: Boolean = true,
    private val callback: Callback
) : ListAdapter<Speaker, SpeakerAdapter.ViewHolder>(SpeakerItemCallback) {

  interface Callback {
    fun onSpeakerClicked(speaker: Speaker)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.item_speaker, parent, false)
    if (!wrapsWidth) {
      v.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }
    return ViewHolder(v, callback)
  }

  internal class ViewHolder(
      itemView: View,
      private val callback: Callback
  ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val image: ImageView = itemView.findViewById(R.id.image)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val namePlaceholder: View = itemView.findViewById(R.id.ph_name)
    private val title: TextView = itemView.findViewById(R.id.title)

    private var speaker: Speaker? = null

    init {
      itemView.setOnClickListener(this)
    }

    fun bind(speaker: Speaker) {
      this.speaker = speaker
      if (speaker.name.isBlank()) {
        namePlaceholder.visibility = View.VISIBLE
        name.visibility = View.GONE
      } else {
        namePlaceholder.visibility = View.GONE
        name.visibility = View.VISIBLE
        name.text = speaker.name
      }

      Glide.with(itemView.context)
          .load(speaker.avatarUrl)
          .asBitmap()
          .placeholder(DrawableUtils.create(itemView.context, R.drawable.ph_speaker))
          .into(image)
    }

    override fun onClick(v: View?) {
      speaker?.let(callback::onSpeakerClicked)
    }
  }

  private object SpeakerItemCallback : ItemCallback<Speaker>() {
    override fun areItemsTheSame(oldItem: Speaker, newItem: Speaker): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Speaker, newItem: Speaker): Boolean {
      return oldItem == newItem
    }
  }
}
