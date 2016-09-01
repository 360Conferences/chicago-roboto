package com.gdgchicagowest.windycitydevcon.features.sessions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.model.Session
import kotlinx.android.synthetic.main.item_session.view.*
import java.text.SimpleDateFormat

internal class SessionAdapter() : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    val sessions: MutableList<Session> = mutableListOf()
    val format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.timeslot.text = "${format.format(session.startTime)}\nto\n${format.format(session.endTime)}"
        holder.title.text = session.name

        val speakers = session.speakers
        if (speakers == null || speakers.isEmpty()) {
            holder.speakers.visibility = View.GONE
        } else {
            holder.speakers.visibility = View.VISIBLE
            holder.speakers.text = session.speakers?.joinToString()
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

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeslot: TextView
        val title: TextView
        val speakers: TextView
        val room: TextView

        init {
            timeslot = super.itemView.timeslot
            title = super.itemView.title
            speakers = super.itemView.speakers
            room = super.itemView.room
        }
    }
}