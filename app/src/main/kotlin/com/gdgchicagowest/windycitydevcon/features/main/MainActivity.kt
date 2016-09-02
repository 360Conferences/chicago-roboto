package com.gdgchicagowest.windycitydevcon.features.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getAppComponent
import com.gdgchicagowest.windycitydevcon.features.sessiondetail.SessionDetailActivity
import com.gdgchicagowest.windycitydevcon.features.sessions.SessionDateView
import com.gdgchicagowest.windycitydevcon.features.sessions.SessionNavigator
import com.gdgchicagowest.windycitydevcon.features.speakerdetail.SpeakerDetailActivity
import com.gdgchicagowest.windycitydevcon.features.speakerdetail.SpeakerNavigator
import com.gdgchicagowest.windycitydevcon.model.Session
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator, NavigationView.OnNavigationItemSelectedListener {
    lateinit var component: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().mainComponent(MainModule(this, this))

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        showView(R.id.action_schedule)

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.action_schedule)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (showView(item.itemId)) {
            drawer_layout.closeDrawers()
            return true
        } else {
            return false
        }
    }

    private fun showView(viewId: Int): Boolean {
        val view = when (viewId) {
            R.id.action_schedule -> SessionDateView(this)
            R.id.action_speakers -> null // TODO
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

    override fun nagivateToSpeaker(id: String, image: View?) {
        SpeakerDetailActivity.navigate(this, id, image)
    }

}