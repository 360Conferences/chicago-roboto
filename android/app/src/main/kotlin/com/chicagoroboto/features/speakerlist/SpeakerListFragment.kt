package com.chicagoroboto.features.speakerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R
import com.chicagoroboto.databinding.SpeakerListBinding
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.sessiondetail.SpeakerAdapter
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.model.Speaker
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Provider

class SpeakerListFragment : Fragment(R.layout.speaker_list) {

  private lateinit var component: SpeakerListComponent

  @Inject lateinit var presenterProvider: Provider<SpeakerListPresenter>
  @Inject lateinit var speakerNavigator: SpeakerNavigator

  private val presentation: Presentation<SpeakerListPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  private val adapter = SpeakerAdapter(false, object : SpeakerAdapter.Callback {
    override fun onSpeakerClicked(speaker: Speaker) {
      speakerNavigator.navigateToSpeaker(speaker.id)
    }
  })

  private lateinit var binding: SpeakerListBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component = context!!.getComponent<MainComponent>().speakerListComponentFactory.create()
    component.inject(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = SpeakerListBinding.bind(view)
    binding.list.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
      adapter = this@SpeakerListFragment.adapter
    }

    binding.appBar.doOnApplyWindowInsets { view, insets, initialState ->
      view.updatePadding(
          top = initialState.paddings.top + insets.systemWindowInsetTop
      )
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect { model ->
        adapter.submitList(model.speakers)
      }
    }
  }
}
