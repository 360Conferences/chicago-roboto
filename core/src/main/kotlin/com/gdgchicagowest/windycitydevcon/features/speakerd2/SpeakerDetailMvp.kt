package com.gdgchicagowest.windycitydevcon.features.speakerdetail

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Speaker

interface SpeakerDetailMvp {

    interface View : Mvp.View {
        fun showSpeaker(speaker: Speaker)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun setSpeakerId(speakerId: String)
    }
}