package com.gdgchicagowest.windycitydevcon.features.location

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getComponent
import com.gdgchicagowest.windycitydevcon.features.main.MainComponent
import com.gdgchicagowest.windycitydevcon.model.Venue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.view_location.view.*
import javax.inject.Inject

class LocationView(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs), LocationMvp.View {

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
        address.text = venue?.street
        city.text = "${venue?.city}, ${venue?.state} ${venue?.zip}"

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