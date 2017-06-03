package com.conferences.features.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.conferences.R
import com.conferences.ext.getAppComponent
import com.conferences.features.info.InfoView
import com.conferences.features.location.LocationView
import com.conferences.features.sessiondetail.SessionDetailActivity
import com.conferences.features.sessions.SessionDateView
import com.conferences.features.sessions.SessionNavigator
import com.conferences.features.speakerdetail.SpeakerDetailActivity
import com.conferences.features.speakerdetail.SpeakerNavigator
import com.conferences.features.speakerlist.SpeakerListView
import com.conferences.model.Session
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator, NavigationView.OnNavigationItemSelectedListener {
    lateinit var component: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = getAppComponent().mainComponent(MainModule(this, this))

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayHomeAsUpEnabled(true)
        }

        setTitle(R.string.event_name)

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
        if (view != null) {
            content.removeAllViews()
            content.addView(view)
            return true
        }
        return false
    }

    override fun getSystemService(name: String?): Any {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }

    override fun showSession(session: Session) {
        val intent = Intent(this, SessionDetailActivity::class.java)
        intent.putExtra("session_id", session.id)
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
