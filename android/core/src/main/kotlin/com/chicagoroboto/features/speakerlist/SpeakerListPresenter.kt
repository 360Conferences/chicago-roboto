package com.chicagoroboto.features.speakerlist

import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.model.Speaker
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.util.*

class SpeakerListPresenter(private val speakerProvider: SpeakerProvider) : SpeakerListMvp.Presenter {

    private var view: SpeakerListMvp.View? = null

    override fun onAttach(view: SpeakerListMvp.View) {
        this.view = view

      launch {
        val speakers = speakerProvider.addSpeakerListener(this).values
        withContext(UI) {
          this@SpeakerListPresenter.view?.showSpeakers(speakers)
        }
      }
    }

    override fun onDetach() {
        this.view = null
    }
}