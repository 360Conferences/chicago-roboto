package com.chicagoroboto.injection

import android.app.Application
import com.chicagoroboto.data.DataModule
import com.chicagoroboto.data.EventId
import com.chicagoroboto.data.SpeakerProvider
import com.chicagoroboto.features.info.InfoComponent
import com.chicagoroboto.features.info.InfoModule
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.main.MainModule
import com.chicagoroboto.features.sessiondetail.SessionDetailComponent
import com.chicagoroboto.features.speakerdetail.SpeakerDetailActivity
import com.chicagoroboto.features.speakerdetail.SpeakerDetailComponent
import com.chicagoroboto.features.speakerlist.SpeakerListComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
  val sessionDetailComponentFactory: SessionDetailComponent.Factory

  fun mainComponent(mainModule: MainModule): MainComponent
  fun infoComponent(): InfoComponent

  fun speakerProvider(): SpeakerProvider

  @Component.Builder
  interface Builder {
    @BindsInstance fun application(app: Application): Builder
    @BindsInstance fun eventId(@EventId eventId: String): Builder
    fun build(): AppComponent
  }
}

