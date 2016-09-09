package com.gdgchicagowest.windycitydevcon.features.info

import com.gdgchicagowest.windycitydevcon.data.LibraryProvider
import com.gdgchicagowest.windycitydevcon.model.Library
import com.gdgchicagowest.windycitydevcon.navigator.WebNavigator

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