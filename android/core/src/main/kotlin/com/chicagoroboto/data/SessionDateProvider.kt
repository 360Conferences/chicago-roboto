package com.chicagoroboto.data

import kotlinx.coroutines.flow.Flow

interface SessionDateProvider {
  fun sessionDates(): Flow<List<String>>
}
