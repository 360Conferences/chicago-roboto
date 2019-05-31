package com.chicagoroboto.features.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator, NavigationView.OnNavigationItemSelectedListener,
        TabHolder {

    private lateinit var tabs: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var content: ViewGroup

    override var tabLayout: TabLayout? = null
        get() = tabs

    private val component: MainComponent by lazy(LazyThreadSafetyMode.NONE) {
        getAppComponent().mainComponent(MainModule(this, this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabs = findViewById(R.id.tabs)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        content = findViewById(R.id.content)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.setDisplayHomeAsUpEnabled(true)
        }

        showView(R.id.action_schedule)

        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.action_schedule)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (showView(item.itemId)) {
            drawerLayout.closeDrawers()
            return true
        } else {
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showView(viewId: Int): Boolean {
        val view: Fragment? = when (viewId) {
            R.id.action_schedule -> SessionDateView()
            R.id.action_speakers -> SpeakerListView()
            R.id.action_location -> LocationView()
            R.id.action_info -> InfoView()
            else -> null
        }

        return view?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, view)
                .commit()
            when (view) {
                is MainView -> view.titleResId
                else -> R.string.app_name
            }
            setTitle(title)
            true
        } ?: false
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            "component" -> component
            else -> super.getSystemService(name)
        }
    }

    override fun showSession(id: String) {
        val intent = Intent(this, SessionDetailActivity::class.java)
                .putExtra("session_id", id)
        startActivity(intent)
    }

    override fun navigateToSpeaker(id: String, image: View?) {
        SpeakerDetailActivity.navigate(this, id, image)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
