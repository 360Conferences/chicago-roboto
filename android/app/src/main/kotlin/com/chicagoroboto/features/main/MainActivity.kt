package com.chicagoroboto.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import com.chicagoroboto.features.info.InfoFragment
import com.chicagoroboto.features.location.LocationFragment
import com.chicagoroboto.features.sessiondetail.SessionDetailActivity
import com.chicagoroboto.features.sessions.SessionDateFragment
import com.chicagoroboto.features.sessions.SessionNavigator
import com.chicagoroboto.features.speakerdetail.SpeakerDetailActivity
import com.chicagoroboto.features.speakerdetail.SpeakerNavigator
import com.chicagoroboto.features.speakerlist.SpeakerListFragment
import com.chicagoroboto.model.Session
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber
import timber.log.error

class MainActivity : AppCompatActivity(), SessionNavigator, SpeakerNavigator {

  private lateinit var navView: BottomNavigationView

  private val component: MainComponent by lazy(LazyThreadSafetyMode.NONE) {
    getAppComponent().mainComponent(MainModule(this, this))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val rootView = findViewById<View>(R.id.root)
    rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    navView = findViewById(R.id.nav_view)
    navView.setOnNavigationItemSelectedListener { item ->
      val tag = item.title
      val fragment = when (item.itemId) {
        R.id.action_schedule -> SessionDateFragment()
        R.id.action_speakers -> SpeakerListFragment()
        R.id.action_location -> LocationFragment()
        R.id.action_info -> InfoFragment()
        else -> {
          Timber.error { "Unknown root view id: ${resources.getResourceName(item.itemId)}" }
          return@setOnNavigationItemSelectedListener false
        }
      }

      supportFragmentManager.beginTransaction()
          .replace(R.id.content, fragment, tag.toString())
          .commit()

      true
    }

    val navViewPadding = navView.paddingBottom
    navView.setOnApplyWindowInsetsListener { view, insets ->
      view.updatePadding(
          bottom = insets.systemWindowInsetBottom + navViewPadding
      )
      insets
    }

    navView.selectedItemId = R.id.action_schedule
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
    if (navView.selectedItemId != R.id.action_schedule) {
      navView.selectedItemId = R.id.action_schedule
    } else {
      super.onBackPressed()
    }
  }
}
