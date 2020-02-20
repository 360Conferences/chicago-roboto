package com.chicagoroboto.features.sessiondetail

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.NotificationProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class SessionDetailPresenterTest {

  val sessionProvider = FakeSessionProvider()
  val speakerProvider = mock<SpeakerProvider>()
  val favoriteProvider = FakeFavoriteProvider()
  val notificationProvider = mock<NotificationProvider>()
  val presenter = SessionDetailPresenter(sessionProvider, speakerProvider, favoriteProvider, notificationProvider)

  @Test fun schedulesFeedbackNotificationWhenFavorited() {
    val session = Session("foo", "Foo Things",
        start_time = "2017-04-20T16:00:00Z",
        end_time = "2017-04-20T16:45:00Z")
    sessionProvider.addSession(session)

    presenter.setSessionId("foo")

    // on
    presenter.toggleFavorite()
    verify(notificationProvider, atLeastOnce()).scheduleFeedbackNotification(eq(session))

    // off
    presenter.toggleFavorite()
    verify(notificationProvider, atLeastOnce()).unscheduleFeedbackNotification(eq(session))
  }

  class FakeSessionProvider : SessionProvider {

    val sessions = mutableMapOf<String, Session>()
    val sessionListeners = mutableMapOf<Any, (sessions: Session?) -> Unit>()
    val sessionListListeners = mutableMapOf<Any, (sessions: List<Session>?) -> Unit>()

    fun addSession(session: Session) {
      sessions[session.id!!] = session
    }

    fun removeSession(session: Session) {
      sessions.remove(session.id!!)
    }

    override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
      sessionListeners[id] = onComplete
      sessions[id]?.let {
        onComplete(it)
      }
    }

    override fun addSessionListener(key: Any, date: String, onComplete: (sessions: List<Session>?) -> Unit) {
      sessionListListeners[key] = onComplete
      sessions.values.filter { it.date == date }.toList().let {
        onComplete(it)
      }
    }

    override fun removeSessionListener(key: Any) {
      sessionListeners.remove(key)
      sessionListListeners.remove(key)
    }

  }

  class FakeFavoriteProvider : FavoriteProvider {

    val favorites = mutableSetOf<String>()
    val listeners = mutableMapOf<String, (sessions: Set<String>) -> Unit>()

    override fun addFavoriteListener(key: String, onComplete: (sessions: Set<String>) -> Unit) {
      listeners[key] = onComplete
      onComplete(favorites)
    }

    override fun removeFavoriteListener(key: String) {
      listeners.remove(key)
    }

    override fun addFavoriteSession(userId: String, id: String) {
      favorites.add(id)
      listeners.values.forEach { it.invoke(favorites) }
    }

    override fun removeFavoriteSession(userId: String, id: String) {
      favorites.remove(id)
      listeners.values.forEach { it.invoke(favorites) }
    }

  }

}
