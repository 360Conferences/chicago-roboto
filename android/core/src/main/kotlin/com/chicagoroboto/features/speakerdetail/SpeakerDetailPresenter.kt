package com.chicagoroboto.features.speakerdetail

import com.chicagoroboto.data.SpeakerProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SpeakerDetailPresenter(val speakerProvider: SpeakerProvider) : SpeakerDetailMvp.Presenter {

    var view: SpeakerDetailMvp.View? = null

    override fun onAttach(view: SpeakerDetailMvp.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun setSpeakerId(speakerId: String) {
      launch {
          val speaker = speakerProvider.getSpeaker(speakerId)
          withContext(UI) {
            view?.showSpeaker(speaker)
          }
      }
    }
}