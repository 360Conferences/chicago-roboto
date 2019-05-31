package com.chicagoroboto.features.location

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chicagoroboto.R
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.features.main.MainView
import com.chicagoroboto.model.Venue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class LocationView : Fragment(), LocationMvp.View, MainView {

    override val titleResId = R.string.action_location

    @Inject lateinit var presenter: LocationMvp.Presenter

    private lateinit var map: MapView
    private lateinit var name: TextView
    private lateinit var address: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.view_location, container, false).apply {
        map = findViewById(R.id.map)
        name = findViewById(R.id.name)
        address = findViewById(R.id.address)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireContext().getComponent<MainComponent>().locationComponent().inject(this)

        map.onCreate(null)
        presenter.onAttach(this)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun showVenue(venue: Venue?) {
        name.text = venue?.name
        address.text = venue?.address

        venue?.let {
            map.getMapAsync { map ->
                val location = LatLng(it.latitude!!, it.longitude!!)
                map.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(location, 17f))
                map.addMarker(MarkerOptions().position(location))
            }
        }
    }
}