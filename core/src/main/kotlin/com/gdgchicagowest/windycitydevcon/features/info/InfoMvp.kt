package com.gdgchicagowest.windycitydevcon.features.info

import com.gdgchicagowest.windycitydevcon.features.shared.Mvp
import com.gdgchicagowest.windycitydevcon.model.Library

interface InfoMvp {

    interface View : Mvp.View {
        fun showLibraries(libraries: List<Library>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun onLibraryClicked(library: Library)
    }
}