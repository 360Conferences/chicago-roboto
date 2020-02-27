package com.chicagoroboto.features.sessions

import android.text.format.DateUtils
import android.text.format.DateUtils.formatDateTime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.databinding.ItemSessionBinding
import com.chicagoroboto.features.sessions.SessionAdapter.ViewHolder
import com.chicagoroboto.features.sessions.SessionListPresenter.Model.Session
import com.chicagoroboto.utils.DrawableUtils
import java.util.*

internal class SessionAdapter(
    private val inflater: LayoutInflater,
    private val callback: Callback
) : ListAdapter<Session, ViewHolder>(SessionItemCallback) {

  interface Callback {
    fun onSessionClicked(session: Session)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = ItemSessionBinding.inflate(inflater, parent, false)
    return ViewHolder(view, callback)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val session = getItem(position)
    val firstInTimeSlot = position == 0 ||
        (getItem(position - 1).session.startTime != session.session.startTime)

    holder.bindSession(session, firstInTimeSlot)
  }

  internal class ViewHolder(
      internal val binding: ItemSessionBinding,
      private val callback: Callback
  ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    var session: Session? = null

    init {
      binding.card.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      session?.let { callback.onSessionClicked(it) }
    }

    fun bindSession(session: Session, isFirstInTimeSlot: Boolean) {
      this.session = session

      val context = binding.root.context
      val startTime = formatDateTime(context, session.session.startTime?.time ?: 0,
          DateUtils.FORMAT_SHOW_TIME)
      val endTime = formatDateTime(context, session.session.endTime?.time ?: 0,
          DateUtils.FORMAT_SHOW_TIME)
      binding.timeslot.text = context.getString(R.string.session_time, startTime, endTime)

      // Dim the session card once the session is over
      val now = Date()
      val bgColor = if (now.before(session.session.endTime)) {
        R.color.session_bg
      } else {
        R.color.session_finished_bg
      }
      binding.card.setBackgroundColor(ContextCompat.getColor(context, bgColor))

      binding.title.text = session.session.title

      if (session.speakers.isEmpty()) {
        binding.speakers.visibility = View.GONE
      } else {
        binding.speakers.visibility = View.VISIBLE
        binding.speakers.text = session.speakers.joinToString { it.name }
        binding.room.setCompoundDrawablesWithIntrinsicBounds(
            DrawableUtils.create(context, R.drawable.ic_speaker),
            null,
            null,
            null)
      }

      if (session.session.location.isBlank()) {
        binding.room.visibility = View.GONE
      } else {
        binding.room.visibility = View.VISIBLE
        binding.room.text = session.session.location
        binding.room.setCompoundDrawablesWithIntrinsicBounds(
            DrawableUtils.create(context, R.drawable.ic_room),
            null,
            null,
            null)
      }

      binding.favorite.visibility = if (session.isFavorite) View.VISIBLE else View.GONE

      if (isFirstInTimeSlot) {
        binding.timeslot.visibility = View.VISIBLE
      } else {
        binding.timeslot.visibility = View.INVISIBLE
      }
    }
  }

  private object SessionItemCallback : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
      return oldItem.session.id == newItem.session.id
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
      return oldItem == newItem
    }

  }
}
