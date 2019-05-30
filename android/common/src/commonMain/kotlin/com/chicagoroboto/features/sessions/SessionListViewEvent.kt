package com.chicagoroboto.features.sessions

sealed class SessionListViewEvent

data class ScrollToSessionIndex(val index: Int) : SessionListViewEvent()