package com.chicagoroboto.features.info

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.VERTICAL
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.features.main.MainView
import com.chicagoroboto.model.Library
import javax.inject.Inject

class InfoView : Fragment(), InfoMvp.View, MainView {

    override val titleResId = R.string.action_info

    @Inject lateinit var presenter: InfoMvp.Presenter

    private lateinit var list: RecyclerView
    private lateinit var adapter: InfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.view_info, container, false).apply {
        list = findViewById(R.id.list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = InfoAdapter { library: Library ->
            presenter.onLibraryClicked(library)
        }
        list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().let {
            it.getAppComponent().infoComponent(InfoModule(it)).inject(this)
        }
        presenter.onAttach(this)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showLibraries(libraries: List<Library>) {
        adapter.setLibraries(libraries)
    }
}