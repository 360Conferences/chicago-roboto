package com.chicagoroboto.features.sessions

data class SessionListViewState(
    val date: String = "",
    val sessions: List<Session> = emptyList()
) {

  data class Session(
      val id: String,
      val title: String,
      val room: String,
      val speakers: List<String>,
      val isFavorite: Boolean
  )
}