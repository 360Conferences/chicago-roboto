package com.chicagoroboto.features.sessions

import android.text.format.DateUtils
import android.text.format.DateUtils.formatDateTime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.features.sessions.SessionAdapter.ViewHolder
import com.chicagoroboto.features.sessions.SessionListPresenter.Model.Session
import com.chicagoroboto.utils.DrawableUtils
import java.util.*

internal class SessionAdapter(
    private val callback: Callback
) : ListAdapter<Session, ViewHolder>(SessionItemCallback) {

  interface Callback {
    fun onSessionClicked(session: Session)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
    return ViewHolder(view, callback)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val session = getItem(position)
    holder.bindSession(session)

    if (position > 0) {
      val previous = getItem(position - 1)
      holder.timeslot.visibility = if (previous.session.startTime == session.session.startTime) {
        View.INVISIBLE
      } else {
        View.VISIBLE
      }
    } else {
      holder.timeslot.visibility = View.VISIBLE
    }
  }

  internal class ViewHolder(
      itemView: View,
      private val callback: Callback
  ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var session: Session? = null

    val timeslot: TextView = itemView.findViewById(R.id.timeslot)
    private val card: CardView = itemView.findViewById(R.id.card)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val speakers: TextView = itemView.findViewById(R.id.speakers)
    private val room: TextView = itemView.findViewById(R.id.room)
    private val favorite: ImageView = itemView.findViewById(R.id.favorite)

    init {
      itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      session?.let { callback.onSessionClicked(it) }
    }

    fun bindSession(session: Session) {
      this.session = session

      val context = itemView.context
      val startTime = formatDateTime(context, session.session.startTime?.time ?: 0,
          DateUtils.FORMAT_SHOW_TIME)
      val endTime = formatDateTime(context, session.session.endTime?.time ?: 0,
          DateUtils.FORMAT_SHOW_TIME)
      timeslot.text = context.getString(R.string.session_time, startTime, endTime)

      // Dim the session card once the session is over
      val now = Date()
      val bgColor = if (now.before(session.session.endTime)) {
        R.color.session_bg
      } else {
        R.color.session_finished_bg
      }
      card.setBackgroundColor(ContextCompat.getColor(context, bgColor))

      title.text = session.session.title

      if (session.speakers.isEmpty()) {
        speakers.visibility = View.GONE
      } else {
        speakers.visibility = View.VISIBLE
        speakers.text = session.speakers.joinToString { it.name }
        room.setCompoundDrawablesWithIntrinsicBounds(
            DrawableUtils.create(context, R.drawable.ic_speaker),
            null,
            null,
            null)
      }

      if (session.session.location.isBlank()) {
        room.visibility = View.GONE
      } else {
        room.visibility = View.VISIBLE
        room.text = session.session.location
        room.setCompoundDrawablesWithIntrinsicBounds(
            DrawableUtils.create(context, R.drawable.ic_room),
            null,
            null,
            null)
      }

      favorite.visibility = if (session.isFavorite) View.VISIBLE else View.GONE
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
