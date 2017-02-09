package com.chicagoroboto.features.speakerdetail

import android.view.View

interface SpeakerNavigator {
    fun nagivateToSpeaker(id: String, image: View? = null)
}