package com.chicagoroboto.features.location

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.chicagoroboto.R
import com.chicagoroboto.ext.getComponent
import com.chicagoroboto.features.main.MainView
import com.chicagoroboto.features.main.MainComponent
import com.chicagoroboto.model.Venue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.view_location.view.*
import javax.inject.Inject

class LocationView(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs), LocationMvp.View, MainView {

    override val titleResId = R.string.action_location

    @Inject lateinit var presenter: LocationMvp.Presenter

    init {
        context.getComponent<MainComponent>().locationComponent().inject(this)

        LayoutInflater.from(context).inflate(R.layout.view_location, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        map.onCreate(null)
        presenter.onAttach(this)
    }

    override fun onDetachedFromWindow() {
        presenter.onDetach()
        super.onDetachedFromWindow()
    }

    override fun showVenue(venue: Venue?) {
        name.text = venue?.name
        address.text = venue?.address

        if (venue != null) {
            map.getMapAsync { map ->
                val location = LatLng(venue.latitude!!, venue.longitude!!)
                map.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(location, 17f))
                map.addMarker(MarkerOptions().position(location))
            }
        }
    }
}