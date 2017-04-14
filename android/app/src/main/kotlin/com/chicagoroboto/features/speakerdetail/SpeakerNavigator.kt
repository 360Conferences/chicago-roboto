package com.chicagoroboto.features.speakerdetail

import android.view.View

interface SpeakerNavigator {
    fun navigateToSpeaker(id: String, image: View? = null)
}