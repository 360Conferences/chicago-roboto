package com.chicagoroboto.features.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.features.TabHolder
import com.chicagoroboto.features.info.InfoView
import com.chicagoroboto.features.location.LocationView
import com.chicagoroboto.features.sessiondetail.SessionDetailActivity
import com.chicagoroboto.features.sessions.SessionDateView
import com.chicagoroboto.features.sessions.SessionNavigator
import com.chicagoroboto.features.speakerdetail.SpeakerDetailActivity
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.features.speakerlist.SpeakerListView
import com.chicagoroboto.model.Session
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator, NavigationView.OnNavigationItemSelectedListener,
        TabHolder {

    override var tabLayout: TabLayout? = null
        get() = tabs

    private val component: MainComponent by lazy(LazyThreadSafetyMode.NONE) {
        getAppComponent().mainComponent(MainModule(this, this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayHomeAsUpEnabled(true)
        }

        showView(R.id.action_schedule)

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.action_schedule)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (showView(item.itemId)) {
            drawer_layout.closeDrawers()
            return true
        } else {
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showView(viewId: Int): Boolean {
        val view: View? = when (viewId) {
            R.id.action_schedule -> SessionDateView(this)
            R.id.action_speakers -> SpeakerListView(this)
            R.id.action_location -> LocationView(this)
            R.id.action_info -> InfoView(this)
            else -> null
        }
        return view?.let {
            content.removeAllViews()
            content.addView(view)
            val title = when (view) {
                is MainView -> view.titleResId
                else -> R.string.app_name
            }
            setTitle(title)
            true
        } ?: false
    }

    override fun getSystemService(name: String?): Any {
        return when (name) {
            "component" -> component
            else -> super.getSystemService(name)
        }
    }

    override fun showSession(session: Session) {
        val intent = Intent(this, SessionDetailActivity::class.java)
                .putExtra("session_id", session.id)
        startActivity(intent)
    }

    override fun navigateToSpeaker(id: String, image: View?) {
        SpeakerDetailActivity.navigate(this, id, image)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
