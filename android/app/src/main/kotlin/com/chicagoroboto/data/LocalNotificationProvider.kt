package com.chicagoroboto.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.chicagoroboto.R
import com.chicagoroboto.features.sessiondetail.SessionDetailActivity
import com.chicagoroboto.model.Session
import com.chicagoroboto.notifications.NotificationPublisher
import com.chicagoroboto.utils.DrawableUtils
import com.chicagoroboto.utils.asBitmap

class LocalNotificationProvider(private val context: Context) : NotificationProvider {

  private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

  override fun scheduleFeedbackNotification(session: Session) {
    val pendingIntent = createPendingIntent(session)
    session.endTime?.time?.let {
      alarmManager.set(AlarmManager.ELAPSED_REALTIME, it, pendingIntent)
    }
  }

  override fun unscheduleFeedbackNotification(session: Session) {
    val pendingIntent = createPendingIntent(session)
    alarmManager.cancel(pendingIntent)
  }

  private fun createPendingIntent(session: Session): PendingIntent {
    val noteBuilder = NotificationCompat.Builder(context, context.getString(R.string.feedback_note_channel))
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(context.getString(R.string.note_feedback_title, session.title))
        .setContentText(context.getString(R.string.note_feedback_msg))
        .setAutoCancel(true)

    DrawableUtils.create(context, R.mipmap.ic_launcher)?.asBitmap().let {
      noteBuilder.setLargeIcon(it)
    }

    val intent = Intent(context, SessionDetailActivity::class.java)
    intent.putExtra("session_id", session.id)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    noteBuilder.setContentIntent(pendingIntent)

    val noteIntent = Intent(context, NotificationPublisher::class.java)
    noteIntent.putExtra("NOTIFICATION", noteBuilder.build())
    noteIntent.putExtra("NOTIFICATION_ID", 0)
    return PendingIntent.getBroadcast(context, 0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT)
  }
}