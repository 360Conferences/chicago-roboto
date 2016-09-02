package com.gdgchicagowest.windycitydevcon.features.sessiondetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gdgchicagowest.windycitydevcon.R
import com.gdgchicagowest.windycitydevcon.ext.getAppComponent
import kotlinx.android.synthetic.main.activity_session_detail.*

class SessionDetailActivity() : AppCompatActivity() {

    private lateinit var component: SessionDetailComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().sessionDetailComponent()

        setContentView(R.layout.activity_session_detail)

        val sessionId = intent.getStringExtra("session_id")
        session_detail.setSession(sessionId)
    }

    override fun getSystemService(name: String?): Any {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }
}