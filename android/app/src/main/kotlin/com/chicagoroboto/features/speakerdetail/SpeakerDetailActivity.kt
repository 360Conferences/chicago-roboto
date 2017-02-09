package com.chicagoroboto.features.speakerdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chicagoroboto.R
import com.chicagoroboto.ext.getAppComponent
import kotlinx.android.synthetic.main.activity_speaker_detail.*

class SpeakerDetailActivity : AppCompatActivity() {

    companion object {
        @JvmStatic fun navigate(activity: Activity, speakerId: String, image: View? = null) {
            val intent = Intent(activity, SpeakerDetailActivity::class.java)
            intent.putExtra("speaker_id", speakerId)

//            if (image != null) {
//                ViewCompat.setTransitionName(image, "image")
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, image, "image")
//                ActivityCompat.startActivity(activity, intent, options.toBundle())
//            } else {
                activity.startActivity(intent)
//            }
        }
    }

    private lateinit var component: SpeakerDetailComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().speakerDetailComponent()

        setContentView(R.layout.activity_speaker_detail)

        val speakerId = intent.getStringExtra("speaker_id")
        speaker_detail_view.setSpeakerId(speakerId)
    }

    override fun getSystemService(name: String?): Any {
        when (name) {
            "component" -> return component
            else -> return super.getSystemService(name)
        }
    }
}