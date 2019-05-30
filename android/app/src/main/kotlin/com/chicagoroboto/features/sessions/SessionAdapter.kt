package com.chicagoroboto.features.sessions

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chicagoroboto.R
import com.chicagoroboto.utils.DrawableUtils
import kotlinx.android.synthetic.main.item_session.view.card
import kotlinx.android.synthetic.main.item_session.view.favorite
import kotlinx.android.synthetic.main.item_session.view.room
import kotlinx.android.synthetic.main.item_session.view.speakers
import kotlinx.android.synthetic.main.item_session.view.timeslot
import kotlinx.android.synthetic.main.item_session.view.title

internal class SessionAdapter(val onSessionSelectedListener: ((id: String) -> Unit)) :
        RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    val sessions: MutableList<SessionListViewState.Session> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view, onSessionSelectedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val session = sessions[position]

        holder.session = session

//        val startTime = formatDateTime(context, session.startTime?.time ?: 0, DateUtils.FORMAT_SHOW_TIME)
//        val endTime = formatDateTime(context, session.endTime?.time ?: 0, DateUtils.FORMAT_SHOW_TIME)
//        holder.timeslot.text = String.format(context.getString(R.string.session_time), startTime, endTime)
//
//        // Dim the session card once hte session is over
//        val now = Date()
//        if (now.before(session.endTime)) {
//            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_bg))
//        } else {
//            holder.card.setBackgroundColor(ContextCompat.getColor(context, R.color.session_finished_bg))
//        }

        holder.title.text = session.title

        if (session.speakers.isEmpty()) {
            holder.speakers.visibility = View.GONE
        } else {
            holder.speakers.visibility = View.VISIBLE
            holder.speakers.text = session.speakers.joinToString()
            holder.room.setCompoundDrawablesWithIntrinsicBounds(
                DrawableUtils.create(context, R.drawable.ic_speaker),
                null,
                null,
                null)
        }

        holder.room.text = session.room
        holder.room.setCompoundDrawablesWithIntrinsicBounds(
            DrawableUtils.create(context, R.drawable.ic_room),
            null,
            null,
            null)

        holder.favorite.visibility = if (session.isFavorite) View.VISIBLE else View.GONE

//        if (position > 0) {
//            val previous = sessions[position - 1]
//            holder.timeslot.visibility = if (previous.startTime == session.startTime) {
//                View.INVISIBLE
//            } else {
//                View.VISIBLE
//            }
//        } else {
//            holder.timeslot.visibility = View.VISIBLE
//        }
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    internal class ViewHolder(itemView: View, private val onSessionSelectedListener: ((id: String) -> Unit)) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var session: SessionListViewState.Session? = null

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
            session?.let {
                onSessionSelectedListener(it.id)
            }
        }
    }
}