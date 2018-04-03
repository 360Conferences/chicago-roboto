package com.chicagoroboto.features.sessions

import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.text.format.DateUtils.formatDateTime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chicagoroboto.R
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.chicagoroboto.utils.DrawableUtils
import kotlinx.android.synthetic.main.item_session.view.*
import java.util.Date

internal class SessionAdapter(val onSessionSelectedListener: ((session: Session) -> Unit)) :
        RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    val sessions: MutableList<Session> = mutableListOf()
    val speakers: MutableMap<String, Speaker> = mutableMapOf()
    val favorites: MutableSet<String> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view, onSessionSelectedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val session = sessions[position]

        holder.session = session

        val startTime = formatDateTime(context, session.startTime?.time ?: 0, DateUtils.FORMAT_SHOW_TIME)
        val endTime = formatDateTime(context, session.endTime?.time ?: 0, DateUtils.FORMAT_SHOW_TIME)
        holder.timeslot.text = String.format(context.getString(R.string.session_time), startTime, endTime)

        // Dim the session card once hte session is over
        val now = Date()
        if (now.before(session.endTime)) {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_bg))
        } else {
            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_finished_bg))
        }

        holder.title.text = session.title

        val sessionSpeakers = session.speakers?.map { speakers[it] }
        if (sessionSpeakers == null || sessionSpeakers.isEmpty()) {
            holder.speakers.visibility = View.GONE
        } else {
            holder.speakers.visibility = View.VISIBLE
            holder.speakers.text = sessionSpeakers.map { it?.name }.joinToString()
            holder.room.setCompoundDrawablesWithIntrinsicBounds(
                DrawableUtils.create(context, R.drawable.ic_speaker),
                null,
                null,
                null)
        }

        if (session.room == null) {
            holder.room.visibility = View.GONE
        } else {
            holder.room.visibility = View.VISIBLE
            holder.room.text = session.room
            holder.room.setCompoundDrawablesWithIntrinsicBounds(
                DrawableUtils.create(context, R.drawable.ic_room),
                null,
                null,
                null)
        }

        if (favorites.contains(session.id)) {
            holder.favorite.visibility = View.VISIBLE
        } else {
            holder.favorite.visibility = View.GONE
        }

        if (position > 0) {
            val previous = sessions[position - 1]
            holder.timeslot.visibility = if (previous.startTime == session.startTime) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        } else {
            holder.timeslot.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    internal class ViewHolder(itemView: View, private val onSessionSelectedListener: ((session: Session) -> Unit)) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var session: Session? = null

        val card: CardView
        val timeslot: TextView
        val title: TextView
        val speakers: TextView
        val room: TextView
        val favorite: ImageView

        init {
            card = super.itemView.card
            timeslot = super.itemView.timeslot
            title = super.itemView.title
            speakers = super.itemView.speakers
            room = super.itemView.room
            favorite = super.itemView.favorite
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (session != null) {
                onSessionSelectedListener(session!!)
            }
        }
    }
}