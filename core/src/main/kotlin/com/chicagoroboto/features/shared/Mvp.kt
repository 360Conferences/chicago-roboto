package com.chicagoroboto.features.shared

interface Mvp {

    interface View {

    }

    interface Presenter<T> {
        fun onAttach(view: T)
        fun onDetach()
    }
}