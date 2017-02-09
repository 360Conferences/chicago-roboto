package com.chicagoroboto.features.info

import com.chicagoroboto.features.shared.Mvp
import com.chicagoroboto.model.Library

interface InfoMvp {

    interface View : Mvp.View {
        fun showLibraries(libraries: List<Library>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun onLibraryClicked(library: Library)
    }
}