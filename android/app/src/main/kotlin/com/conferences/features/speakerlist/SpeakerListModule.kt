package com.conferences.features.speakerlist

import com.conferences.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerListModule {

    @Provides fun speakerListPresenter(speakerProvider: SpeakerProvider): SpeakerListMvp.Presenter {
        return SpeakerListPresenter(speakerProvider)
    }

}