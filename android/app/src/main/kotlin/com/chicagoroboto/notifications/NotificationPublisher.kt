package com.chicagoroboto.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationPublisher : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val noteManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val note = intent.getParcelableExtra<Notification>("NOTIFICATION")
    val noteId = intent.getIntExtra("NOTIFICATION_ID", 0)
    noteManager.notify(noteId, note)
  }

}