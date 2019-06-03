package com.chicagoroboto.features.speakerlist

import android.content.Context
import android.os.Bundle
import androidx.core.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.VERTICAL
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chicagoroboto.R
import com.chicagoroboto.data.AvatarProvider
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainView
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.sessiondetail.SpeakerAdapter
import com.chicagoroboto.features.sessions.DividerItemDecoration
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.model.Speaker
import javax.inject.Inject

class SpeakerListView : Fragment(),
        SpeakerListMvp.View, MainView {

    override val titleResId = R.string.action_speakers

    private lateinit var list: RecyclerView

    private lateinit var adapter: SpeakerAdapter

    @Inject lateinit var presenter: SpeakerListMvp.Presenter
    @Inject lateinit var speakerNavigator: SpeakerNavigator
    @Inject lateinit var avatarProvider: AvatarProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.view_speaker_list, container, false).apply {
        list = findViewById(R.id.list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().getComponent<MainComponent>().speakerListComponent().inject(this)

        list.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        list.addItemDecoration(DividerItemDecoration(requireContext()))
        adapter = SpeakerAdapter(avatarProvider, false) { speaker, v ->
            speakerNavigator.navigateToSpeaker(speaker.id!!, v)
        }
        list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().getComponent<MainComponent>().speakerListComponent().inject(this)

        presenter.onAttach(this)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showSpeakers(speakers: Collection<Speaker>) {
        adapter.speakers.clear()
        adapter.speakers.addAll(speakers)
        adapter.notifyDataSetChanged()
    }
}