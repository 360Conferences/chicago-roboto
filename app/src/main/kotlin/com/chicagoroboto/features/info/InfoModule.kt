package com.chicagoroboto.features.info

import android.content.Context
import com.chicagoroboto.data.AndroidLibraryProvider
import com.chicagoroboto.data.LibraryProvider
import com.chicagoroboto.navigator.AndroidWebNavigator
import com.chicagoroboto.navigator.WebNavigator
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