package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Speaker

class SpeakerListPresenter(private val speakerProvider: SpeakerProvider) : SpeakerListMvp.Presenter {

    private var view: SpeakerListMvp.View? = null

    override fun onAttach(view: SpeakerListMvp.View) {
        this.view = view

        speakerProvider.addSpeakerListener(this) { speakers: Map<String, Speaker>? ->
            if (speakers != null) {
                this.view?.showSpeakers(ArrayList(speakers.values))
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }
}