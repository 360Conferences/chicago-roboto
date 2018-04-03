package com.chicagoroboto.features.speakerdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chicagoroboto.R
import com.chicagoroboto.ext.getActivity
import com.chicagoroboto.ext.getAppComponent
import kotlinx.android.synthetic.main.activity_speaker_detail.*
import java.util.ArrayList

class SpeakerDetailActivity : AppCompatActivity() {

    companion object {
        @JvmStatic fun navigate(activity: Activity, speakerId: String, image: View? = null) {
            val intent = Intent(activity, SpeakerDetailActivity::class.java)
            intent.putExtra("speaker_id", speakerId)

            // FIXME: the shared image is transition properly. The start/end locations are off
//            if (image != null) {
//                ViewCompat.setTransitionName(image, "image_$speakerId")
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
//                    *buildTransitionPairs(image))
//                ActivityCompat.startActivity(activity, intent, options.toBundle())
//            } else {
                activity.startActivity(intent)
//            }
        }

        @SuppressLint("NewApi") @JvmStatic
        private fun buildTransitionPairs(view: View): Array<Pair<View, String>> {
            val pairs = ArrayList<Pair<View, String>>()

            view.getActivity()?.let {
                val decor = it.window.decorView

                val statusBarBackground: View = decor.findViewById(android.R.id.statusBarBackground)
                if (statusBarBackground != null) {
                    pairs.add(Pair.create(statusBarBackground, statusBarBackground.transitionName))
                }

                val navBar: View = decor.findViewById(android.R.id.navigationBarBackground)
                if (navBar != null) {
                    pairs.add(Pair.create(navBar, navBar.transitionName))
                }

                pairs.add(Pair.create(view, view.transitionName))

                return pairs.toTypedArray()
            }

            return pairs.toTypedArray()
        }
    }

    private lateinit var component: SpeakerDetailComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent().speakerDetailComponent()

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
        }

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