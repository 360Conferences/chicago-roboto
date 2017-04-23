package com.conferences.features.info

import android.content.Context
import com.conferences.data.AndroidLibraryProvider
import com.conferences.data.LibraryProvider
import com.conferences.navigator.AndroidWebNavigator
import com.conferences.navigator.WebNavigator
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