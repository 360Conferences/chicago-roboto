package com.conferences.features.info

import com.conferences.features.shared.Mvp
import com.conferences.model.Library

interface InfoMvp {

    interface View : Mvp.View {
        fun showLibraries(libraries: List<Library>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun onLibraryClicked(library: Library)
    }
}