package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker

interface SpeakerProvider {
    suspend fun getSpeakersMap(key: Any): Map<String, Speaker>
    suspend fun getSpeaker(id: String) : Speaker
    fun addSpeakerListener(id: String, onComplete: (speaker: Speaker?) -> Unit)
    fun removeSpeakerListener(key: Any)
}