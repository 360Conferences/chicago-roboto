package com.conferences.features.speakerlist

import com.conferences.data.SpeakerProvider
import com.conferences.model.Speaker
import java.util.*

class SpeakerListPresenter(private val speakerProvider: SpeakerProvider) : SpeakerListMvp.Presenter {

    private var view: SpeakerListMvp.View? = null

    override fun onAttach(view: SpeakerListMvp.View) {
        this.view = view

        speakerProvider.addSpeakerListener(this, { speakers: Map<String, Speaker>? ->
            if (speakers != null) {
                this.view?.showSpeakers(ArrayList<Speaker>(speakers.values))
            }
        })
    }

    override fun onDetach() {
        this.view = null
    }
}