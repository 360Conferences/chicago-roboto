package com.gdgchicagowest.windycitydevcon.features.sessions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.model.Session
import com.gdgchicagowest.windycitydevcon.model.Speaker
import kotlinx.android.synthetic.main.item_session.view.*
import java.text.SimpleDateFormat

internal class SessionAdapter(val onSessionSelectedListener: ((session: Session) -> Unit)) :
        RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    val sessions: MutableList<Session> = mutableListOf()
    val speakers: MutableMap<String, Speaker> = mutableMapOf()
    val format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view, onSessionSelectedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.session = session
        holder.timeslot.text = "${format.format(session.startTime)}\nto\n${format.format(session.endTime)}"
        holder.title.text = session.name

        val sessionSpeakers = session.speakers?.map { speakers[it] }
        if (sessionSpeakers == null || sessionSpeakers.isEmpty()) {
            holder.speakers.visibility = View.GONE
        } else {
            holder.speakers.visibility = View.VISIBLE
            holder.speakers.text = sessionSpeakers.map { it?.name }.joinToString()
        }

        if (session.room == null) {
            holder.room.visibility = View.GONE
        } else {
            holder.room.visibility = View.VISIBLE
            holder.room.text = session.room
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

        val timeslot: TextView
        val title: TextView
        val speakers: TextView
        val room: TextView

        init {
            timeslot = super.itemView.timeslot
            title = super.itemView.title
            speakers = super.itemView.speakers
            room = super.itemView.room
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (session != null) {
                onSessionSelectedListener(session!!)
            }
        }
    }
}