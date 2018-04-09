package com.chicagoroboto.features.info

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.features.main.MainView
import com.chicagoroboto.model.Library
import javax.inject.Inject

class InfoView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs), InfoMvp.View, MainView {

    override val titleResId = R.string.action_info

    @Inject lateinit var presenter: InfoMvp.Presenter

    private val adapter: InfoAdapter

    init {
        context.getAppComponent().infoComponent(InfoModule(context)).inject(this)

        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = InfoAdapter({ library: Library ->
            presenter.onLibraryClicked(library)
        })
        super.setAdapter(adapter)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    override fun showLibraries(libraries: List<Library>) {
        adapter.setLibraries(libraries)
    }
}