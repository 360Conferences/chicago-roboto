package com.chicagoroboto.data

import com.chicagoroboto.model.Library
import kotlinx.coroutines.flow.Flow

interface LibraryProvider {
  fun libraries(): Flow<List<Library>>
}
