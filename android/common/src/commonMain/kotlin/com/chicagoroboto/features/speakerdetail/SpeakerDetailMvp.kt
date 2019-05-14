package com.chicagoroboto.features.speakerdetail

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Speaker

interface SpeakerDetailMvp {

    interface View : Mvp.View {
        fun showSpeaker(speaker: Speaker)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSpeakerId(speakerId: String)
    }
}