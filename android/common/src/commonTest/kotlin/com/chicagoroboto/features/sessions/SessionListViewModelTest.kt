package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.FavoriteProvider
import com.chicagoroboto.data.SessionProvider
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Session
import com.chicagoroboto.model.Speaker
import com.ryanharter.observable.Observer
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SessionListViewModelTest {

  private lateinit var sessionProvider: FakeSessionProvider
  private lateinit var speakerProvider: FakeSpeakerProvider
  private lateinit var favoriteProvider: FakeFavoriteProvider

  private lateinit var viewModel: SessionListViewModel
  private lateinit var observer: TestObserver<SessionListViewState>

  @BeforeTest
  fun setup() {
    observer = TestObserver()
    sessionProvider = FakeSessionProvider()
    speakerProvider = FakeSpeakerProvider()
    favoriteProvider = FakeFavoriteProvider()
    viewModel = SessionListViewModel(sessionProvider, speakerProvider, favoriteProvider)
  }

  @Test
  fun `returns no data before date is set`() {
    viewModel.viewState.observe(observer)

    val data = observer.lastValue
    assertNull(data)
  }

  // TODO These are really testing MediatorObserver, so should be moved there.
  @Test
  fun `doesn't add listeners until observer is active`() {
    assertTrue(sessionProvider.sessionByDateListeners.isEmpty())
    assertTrue(speakerProvider.speakerListeners.isEmpty())
    assertTrue(favoriteProvider.favoriteListeners.isEmpty())
  }

  @Test
  fun `adds listeners once observer is active`() {
    viewModel.viewState.observe(observer)
    viewModel.setDate("foo")
    assertFalse(sessionProvider.sessionByDateListeners.isEmpty())
    assertFalse(speakerProvider.speakerListeners.isEmpty())
    assertFalse(favoriteProvider.favoriteListeners.isEmpty())
  }

  @Test
  fun `returns data after date is set`() {
    viewModel.viewState.observe(observer)

    val expected = SessionListViewState(
        date = "date",
        sessions = listOf(
            SessionListViewState.Session("1", "session 1", "main", listOf("foo", "bar"), false),
            SessionListViewState.Session("2", "session 2", "second", listOf("foo"), false),
            SessionListViewState.Session("3", "session 3", "third", listOf("bar"), true)
        )
    )

    val s = Session(date = "date")
    sessionProvider.sessions = listOf(
        s.copy(id = "1", title = "session 1", speakers = listOf("1", "2"), location = "main"),
        s.copy(id = "2", title = "session 2", speakers = listOf("1"), location = "second"),
        s.copy(id = "3", title = "session 3", speakers = listOf("2"), location = "third")
    )
    speakerProvider.speakers = listOf(
        Speaker(id = "1", name = "foo"),
        Speaker(id = "2", name = "bar")
    )
    favoriteProvider.favoriteSessions = setOf("3")

    viewModel.setDate("date")

    val data = observer.lastValue
    assertEquals(expected, data)
  }

  class TestObserver<T> : Observer<T> {

    val values = mutableListOf<T>()
    val lastValue: T?
      get() = values.lastOrNull()

    override fun invoke(value: T) {
      values.add(value)
    }
  }

  class FakeSessionProvider : SessionProvider {

    var sessionsSet = false
    var sessions: List<Session> = emptyList()
      set(value) {
        field = value
        sessionsSet = true
        dispatchSessions()
      }

    val sessionListeners = mutableMapOf<String, (Session?) -> Unit>()
    val sessionByDateListeners = mutableMapOf<Any, SessionByDateListener>()

    data class SessionByDateListener(val date: String, val onComplete: (List<Session>?) -> Unit)

    override fun addSessionListener(id: String, onComplete: (Session?) -> Unit) {
      sessionListeners[id] = onComplete

      if (sessionsSet) {
        dispatchSessions()
      }
    }

    override fun addSessionListener(
        key: Any,
        date: String,
        onComplete: (sessions: List<Session>?) -> Unit
    ) {
      sessionByDateListeners[key] = SessionByDateListener(date, onComplete)

      if (sessionsSet) {
        dispatchSessions()
      }
    }

    override fun removeSessionListener(key: Any) {
      when {
        sessionByDateListeners.containsKey(key) -> sessionByDateListeners.remove(key)
        key is String && sessionListeners.containsKey(key) -> sessionListeners.remove(key)
        else -> throw IllegalArgumentException("No listener registered for key: $key")
      }
    }

    private fun dispatchSessions() {
      sessionListeners.forEach { (id, onComplete) -> onComplete(sessions.find { it.id == id }) }
      sessionByDateListeners.values.forEach { listener ->
        listener.onComplete(sessions.filter { it.date == listener.date })
      }
    }

  }

  class FakeSpeakerProvider : SpeakerProvider {

    var speakersSet = false
    var speakers: List<Speaker> = emptyList()
      set(value) {
        field = value
        speakersSet = true
        dispatchSpeakers()
      }

    val speakerListeners = mutableMapOf<Any, (Map<String, Speaker>?) -> Unit>()
    val speakerByIdListeners = mutableMapOf<String, (Speaker?) -> Unit>()

    override fun addSpeakerListener(
        key: Any,
        onComplete: (speakers: Map<String, Speaker>?) -> Unit
    ) {
      speakerListeners[key] = onComplete

      if (speakersSet) {
        dispatchSpeakers()
      }
    }

    override fun addSpeakerListener(id: String, onComplete: (speaker: Speaker?) -> Unit) {
      speakerByIdListeners[id] = onComplete
      if (speakersSet) {
        dispatchSpeakers()
      }
    }

    override fun removeSpeakerListener(key: Any) {
      when {
        speakerListeners.containsKey(key) -> speakerListeners.remove(key)
        key is String && speakerByIdListeners.containsKey(key) -> speakerByIdListeners.remove(key)
        else -> throw IllegalArgumentException("No listener registered for key: $key")
      }
    }

    private fun dispatchSpeakers() {
      speakerListeners.values.forEach { it(speakers.associateBy { it.id!! }) }
      speakerByIdListeners.forEach { (id, onComplete) ->
        onComplete(speakers.find { it.id == id })
      }
    }

  }

  class FakeFavoriteProvider : FavoriteProvider {

    var favoriteSessions: Set<String> = emptySet()
    val favoriteListeners = mutableMapOf<String, (Set<String>) -> Unit>()

    override fun addFavoriteListener(key: String, onComplete: (sessions: Set<String>) -> Unit) {
      favoriteListeners[key] = onComplete
      dispatchFavorites()
    }

    override fun removeFavoriteListener(key: String) {
      favoriteListeners.remove(key)
          ?: throw IllegalArgumentException("No listener registered for key: $key")
    }

    override fun addFavoriteSession(id: String) {
      favoriteSessions = favoriteSessions.toMutableSet() + id
      dispatchFavorites()
    }

    override fun removeFavoriteSession(id: String) {
      favoriteSessions = favoriteSessions.toMutableSet() - id
      dispatchFavorites()
    }

    private fun dispatchFavorites() {
      favoriteListeners.values.forEach { it(favoriteSessions) }
    }

  }
}