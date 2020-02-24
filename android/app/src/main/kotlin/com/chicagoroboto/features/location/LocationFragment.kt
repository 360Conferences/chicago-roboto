package com.chicagoroboto.features.location

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chicagoroboto.R
import com.chicagoroboto.databinding.LocationTabBinding
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.ext.presentations
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.shared.Presentation
import com.chicagoroboto.features.shared.startPresentation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Provider

class LocationFragment : Fragment(R.layout.location_tab) {

  private val presentation: Presentation<LocationPresenter> by presentations {
    presenterProvider.get().startPresentation(Dispatchers.Main)
  }

  @Inject lateinit var presenterProvider: Provider<LocationPresenter>

  private lateinit var binding: LocationTabBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = LocationTabBinding.bind(view)
    binding.map.onCreate(null)

    binding.appBar.doOnApplyWindowInsets { view, insets, initialState ->
      view.updatePadding(
          top = initialState.paddings.top + insets.systemWindowInsetTop
      )
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    context!!.getComponent<MainComponent>().locationComponent().inject(this)

    lifecycleScope.launchWhenStarted {
      presentation.presenter.models.collect {
        binding.name.text = it.venue.name
        binding.address.text = it.venue.address
        binding.map.getMapAsync { map ->
          val location = LatLng(it.venue.latitude, it.venue.longitude)
          map.moveCamera(CameraUpdateFactory
              .newLatLngZoom(location, 17f))
          map.addMarker(MarkerOptions().position(location))
        }
      }
    }
  }
}
