package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.flow.Flow

interface SpeakerProvider {
  fun speakers(): Flow<List<Speaker>>
  fun speaker(speakerId: String): Flow<Speaker>
  suspend fun avatar(speakerId: String): String
}
