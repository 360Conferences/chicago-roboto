package com.gdgchicagowest.windycitydevcon.features.speakerdetail

import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import com.gdgchicagowest.windycitydevcon.model.Speaker

class SpeakerDetailPresenter(val speakerProvider: SpeakerProvider) : SpeakerDetailMvp.Presenter {

    var view: SpeakerDetailMvp.View? = null

    override fun onAttach(view: SpeakerDetailMvp.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun setSpeakerId(speakerId: String) {
        speakerProvider.addSpeakerListener(speakerId, { speaker: Speaker? ->
            if (speaker != null) {
                view?.showSpeaker(speaker)
            }
        })
    }
}