package com.chicagoroboto.features.info

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.chicagoroboto.R
import com.chicagoroboto.databinding.InfoBinding
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.info.InfoPresenter.Event.ClickedLibrary
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import com.chicagoroboto.view.MotionLayoutInsetHelper
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Provider

class InfoFragment : Fragment(R.layout.info) {

  private val presentation: Presentation<InfoPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  @Inject
  lateinit var presenterProvider: Provider<InfoPresenter>

  private val motionLayoutInsetHelper = MotionLayoutInsetHelper()
  private lateinit var binding: InfoBinding
  private val adapter = InfoAdapter { library ->
    presentation.presenter.events.offer(ClickedLibrary(library))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = InfoBinding.bind(view)

    binding.motionLayoutRoot.setTransitionListener(object : TransitionAdapter() {

      override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        val typefaceStyle = if (endId == R.id.expanded) Typeface.BOLD else Typeface.NORMAL
        binding.toolbarText.setTypeface(binding.toolbarText.typeface, typefaceStyle)
      }
    })

    binding.list.apply {
      layoutManager = LinearLayoutManager(context, VERTICAL, false)
      adapter = this@InfoFragment.adapter
    }

    binding.inset.doOnApplyWindowInsets { _, insets, _ ->
      view.updateLayoutParams { height = insets.systemWindowInsetTop }
    }

    binding.contributeImg.setOnClickListener {
      startActivity(Intent(ACTION_VIEW, Uri.parse(getString(R.string.github_link))))
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    context!!.getAppComponent().infoComponent().inject(this)

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect {
        adapter.setLibraries(it.libraries)
      }
    }
  }
}
