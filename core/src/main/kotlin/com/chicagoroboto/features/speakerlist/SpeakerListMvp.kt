package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Speaker

interface SpeakerListMvp {

    interface View : Mvp.View {
        fun showSpeakers(speakers: Collection<Speaker>)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}