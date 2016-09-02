package com.gdgchicagowest.windycitydevcon.features.sessions

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getAppComponent
import com.gdgchicagowest.windycitydevcon.features.sessiondetail.SessionDetailActivity
import com.gdgchicagowest.windycitydevcon.model.Session
import javax.inject.Inject

class SessionListActivity : AppCompatActivity(), SessionDisplay {

    @Inject lateinit var component: SessionListComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().sessionListComponent(SessionListModule(this))

        setContentView(R.layout.activity_session_list)
    }

    override fun getSystemService(name: String?): Any {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }

    override fun showSession(session: Session) {
        // TODO Check if dual pane
        val intent = Intent(this, SessionDetailActivity::class.java)
        intent.putExtra("session_id", session.id)
        startActivity(intent)
    }
}