package com.chicagoroboto.features.info

import com.chicagoroboto.data.LibraryProvider
import com.chicagoroboto.model.Library
import com.chicagoroboto.navigator.WebNavigator

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