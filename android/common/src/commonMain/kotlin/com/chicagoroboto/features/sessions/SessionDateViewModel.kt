package com.chicagoroboto.features.sessions

import com.chicagoroboto.data.SessionDateProvider
import com.chicagoroboto.features.shared.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.flow.asPublisher

data class SessionDateViewState(val dates: List<String>)

class SessionDateViewModel(
    private val sessionDateProvider: SessionDateProvider
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val viewState = sessionDateProvider.getSessionDates()
        .map { SessionDateViewState(dates = it) }
        .asPublisher()

}