package com.chicagoroboto.data

import com.chicagoroboto.model.Speaker

interface SpeakerProvider {
    suspend fun addSpeakerListener(key: Any): Map<String, Speaker>
    fun addSpeakerListener(id: String, onComplete: (speaker: Speaker?) -> Unit)
    fun removeSpeakerListener(key: Any)
}