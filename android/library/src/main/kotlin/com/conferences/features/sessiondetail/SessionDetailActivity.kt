package com.conferences.features.sessiondetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.conferences.R
import com.conferences.ext.getAppComponent
import com.conferences.features.speakerdetail.SpeakerDetailActivity
import com.conferences.features.speakerdetail.SpeakerNavigator
import kotlinx.android.synthetic.main.activity_session_detail.*

class SessionDetailActivity : AppCompatActivity(), SpeakerNavigator {
    private lateinit var component: SessionDetailComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().sessionDetailComponent(SessionDetailModule(this))

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

    override fun navigateToSpeaker(id: String, image: View?) {
        SpeakerDetailActivity.navigate(this, id, image)
    }

}
