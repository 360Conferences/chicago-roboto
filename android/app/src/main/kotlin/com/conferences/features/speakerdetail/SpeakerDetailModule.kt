package com.conferences.features.speakerdetail

import com.conferences.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerDetailModule {
    @Provides fun speakerDetailPresenter(speakerProvider: SpeakerProvider): SpeakerDetailMvp.Presenter {
        return SpeakerDetailPresenter(speakerProvider)
    }
}