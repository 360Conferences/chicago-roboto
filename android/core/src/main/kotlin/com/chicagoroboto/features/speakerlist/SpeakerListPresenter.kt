package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.data.SpeakerProvider
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SpeakerListPresenter(private val speakerProvider: SpeakerProvider) : SpeakerListMvp.Presenter {

    private var view: SpeakerListMvp.View? = null

    override fun onAttach(view: SpeakerListMvp.View) {
        this.view = view

      launch {
        val speakers = speakerProvider.getSpeakersMap(this).values
        withContext(UI) {
          this@SpeakerListPresenter.view?.showSpeakers(speakers)
        }
      }
    }

    override fun onDetach() {
        this.view = null
    }
}