package com.gdgchicagowest.windycitydevcon.features.info

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.gdgchicagowest.windycitydevcon.ext.getAppComponent
import com.gdgchicagowest.windycitydevcon.model.Library
import javax.inject.Inject

class InfoView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs), InfoMvp.View {

    @Inject lateinit var presenter: InfoMvp.Presenter

    private lateinit var adapter: InfoAdapter

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