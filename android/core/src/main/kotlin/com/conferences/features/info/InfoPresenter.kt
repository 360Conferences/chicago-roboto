package com.conferences.features.info

import com.conferences.data.LibraryProvider
import com.conferences.model.Library
import com.conferences.navigator.WebNavigator

class InfoPresenter(val libraryProvider: LibraryProvider, val webNavigator: WebNavigator) : InfoMvp.Presenter {

    private var view: InfoMvp.View? = null

    override fun onAttach(view: InfoMvp.View) {
        this.view = view
        libraryProvider.libraries { libraries ->
            this.view?.showLibraries(libraries)
        }
    }

    override fun onDetach() {
        this.view = null
    }

    override fun onLibraryClicked(library: Library) {
        webNavigator.navigateToUrl(library.url)
    }
}