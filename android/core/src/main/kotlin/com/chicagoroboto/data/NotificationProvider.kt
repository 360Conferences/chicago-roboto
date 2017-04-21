package com.chicagoroboto.data

import com.chicagoroboto.model.Session

interface NotificationProvider {
  fun scheduleFeedbackNotification(session: Session)
  fun unscheduleFeedbackNotification(session: Session)
}