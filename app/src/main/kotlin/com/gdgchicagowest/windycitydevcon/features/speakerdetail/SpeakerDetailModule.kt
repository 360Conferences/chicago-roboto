package com.gdgchicagowest.windycitydevcon.features.speakerdetail

import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerDetailModule {
    @Provides fun speakerDetailPresenter(speakerProvider: SpeakerProvider): SpeakerDetailMvp.Presenter {
        return SpeakerDetailPresenter(speakerProvider)
    }
}