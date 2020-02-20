package com.chicagoroboto.features.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator, NavigationView.OnNavigationItemSelectedListener,
    TabHolder {

  override val tabLayout: TabLayout?
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

    // Sure don't like this here, should definitely be handled elsewhere.
    component.userProvider.signIn {
      showView(R.id.action_speakers)

      nav_view.setNavigationItemSelectedListener(this)
      nav_view.setCheckedItem(R.id.action_speakers)
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        drawer_layout.openDrawer(GravityCompat.START)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun showView(viewId: Int): Boolean {
    val view: Fragment = when (viewId) {
//      R.id.action_schedule -> SessionDateView(this)
      R.id.action_speakers -> SpeakerListView()
//      R.id.action_location -> LocationView(this)
//      R.id.action_info -> InfoView(this)
      else -> return false
    }

    supportFragmentManager.beginTransaction()
        .replace(R.id.content, view)
        .commit()

    return true
  }

  override fun getSystemService(name: String): Any? {
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
