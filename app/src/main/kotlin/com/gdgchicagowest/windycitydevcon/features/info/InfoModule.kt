package com.gdgchicagowest.windycitydevcon.features.info

import android.content.Context
import com.gdgchicagowest.windycitydevcon.data.AndroidLibraryProvider
import com.gdgchicagowest.windycitydevcon.data.LibraryProvider
import com.gdgchicagowest.windycitydevcon.navigator.AndroidWebNavigator
import com.gdgchicagowest.windycitydevcon.navigator.WebNavigator
import dagger.Module
import dagger.Provides

@Module
class InfoModule(private val context: Context) {
    @Provides fun libraryProvider(): LibraryProvider {
        return AndroidLibraryProvider(context)
    }

    @Provides fun WebNavigator(): WebNavigator {
        return AndroidWebNavigator(context)
    }

    @Provides fun infoPresenter(libraryProvider: LibraryProvider, webNavigator: WebNavigator): InfoMvp.Presenter {
        return InfoPresenter(libraryProvider, webNavigator)
    }
}