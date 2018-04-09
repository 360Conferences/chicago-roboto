package com.chicagoroboto.features.speakerlist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
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

class SpeakerListView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        RecyclerView(context, attrs, defStyle),
        SpeakerListMvp.View, MainView {

    override val titleResId = R.string.action_speakers

    private val adapter: SpeakerAdapter

    @Inject lateinit var presenter: SpeakerListMvp.Presenter
    @Inject lateinit var speakerNavigator: SpeakerNavigator
    @Inject lateinit var avatarProvider: AvatarProvider

    init {
        context.getComponent<MainComponent>().speakerListComponent().inject(this)

        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        addItemDecoration(DividerItemDecoration(context))
        adapter = SpeakerAdapter(avatarProvider, false, { speaker, view ->
            speakerNavigator.navigateToSpeaker(speaker.id!!, view)
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

    override fun showSpeakers(speakers: Collection<Speaker>) {
        adapter.speakers.clear()
        adapter.speakers.addAll(speakers)
        adapter.notifyDataSetChanged()
    }
}