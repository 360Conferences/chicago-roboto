package com.chicagoroboto.data

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.chicagoroboto.R
import com.chicagoroboto.features.sessiondetail.SessionDetailActivity
import com.chicagoroboto.model.Session
import com.chicagoroboto.notifications.NotificationPublisher
import com.chicagoroboto.utils.DrawableUtils
import com.chicagoroboto.utils.asBitmap
import javax.inject.Inject

class LocalNotificationProvider @Inject constructor(
    private val application: Application
) : NotificationProvider {

  private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
    val noteBuilder =
        NotificationCompat.Builder(application, application.getString(R.string.feedback_note_channel))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(application.getString(R.string.note_feedback_title, session.title))
            .setContentText(application.getString(R.string.note_feedback_msg))
            .setAutoCancel(true)

    DrawableUtils.create(application, R.mipmap.ic_launcher)?.asBitmap().let {
      noteBuilder.setLargeIcon(it)
    }

    val intent = Intent(application, SessionDetailActivity::class.java)
    intent.putExtra("session_id", session.id)
    val pendingIntent = PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    noteBuilder.setContentIntent(pendingIntent)

    val noteIntent = Intent(application, NotificationPublisher::class.java)
    noteIntent.putExtra("NOTIFICATION", noteBuilder.build())
    noteIntent.putExtra("NOTIFICATION_ID", 0)
    return PendingIntent.getBroadcast(application, 0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT)
  }
}
