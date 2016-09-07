package com.gdgchicagowest.windycitydevcon.features.speakerlist

import com.gdgchicagowest.windycitydevcon.data.SpeakerProvider
import dagger.Module
import dagger.Provides

@Module
class SpeakerListModule {

    @Provides fun speakerListPresenter(speakerProvider: SpeakerProvider): SpeakerListMvp.Presenter {
        return SpeakerListPresenter(speakerProvider)
    }

}