package com.gdgchicagowest.windycitydevcon.features.speakerlist

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Speaker

interface SpeakerListMvp {

    interface View : Mvp.View {
        fun showSpeakers(speakers: Collection<Speaker>)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}