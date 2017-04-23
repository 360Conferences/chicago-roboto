package com.conferences.features.speakerdetail

import com.conferences.features.shared.Mvp
import com.conferences.model.Speaker

interface SpeakerDetailMvp {

    interface View : Mvp.View {
        fun showSpeaker(speaker: Speaker)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSpeakerId(speakerId: String)
    }
}