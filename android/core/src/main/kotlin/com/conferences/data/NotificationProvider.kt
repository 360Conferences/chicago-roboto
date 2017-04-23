package com.conferences.data

import com.conferences.model.Session

interface NotificationProvider {
  fun scheduleFeedbackNotification(session: Session)
  fun unscheduleFeedbackNotification(session: Session)
}