package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.flow.Flow

interface SpeakerProvider {
  fun speaker(speakerId: String): Flow<Speaker>
  suspend fun avatar(speakerId: String): String
  fun addSpeakerListener(key: Any, onComplete: (speakers: Map<String, Speaker>?) -> Unit)
  fun addSpeakerListener(id: String, onComplete: (speaker: Speaker?) -> Unit)
  fun removeSpeakerListener(key: Any)
}
