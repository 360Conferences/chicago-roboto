package com.conferences.features.speakerlist

import com.conferences.features.shared.Mvp
import com.conferences.model.Speaker

interface SpeakerListMvp {

    interface View : Mvp.View {
        fun showSpeakers(speakers: Collection<Speaker>)
    }

    interface Presenter : Mvp.Presenter<View> {

    }
}