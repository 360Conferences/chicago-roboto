package com.chicagoroboto.utils

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

/**
 * Adapter base class to optionally add animations to the view items
 */
open class ChicagoRobotoAdapter<T: RecyclerView.ViewHolder>(private var mLastPosition:Int = -1,
                                                            var mAnimationType:Int = android.R.anim.slide_in_left,
                                                            var mAnimationDuration:Long = ANIMATION_DURATION,
                                                            var mShowAnimation:Boolean = true) :
        RecyclerView.Adapter<T>() {

    companion object {
        const val ANIMATION_DURATION: Long = 1000
    }

    /**
     * These won't be used, but will be implemented by child adapter classes.
     * There's currently no worry on the TODOs getting hit at runtime but if children ever call it
     * the app can crash.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Play the animation
     */
    override fun onBindViewHolder(holder: T, position: Int) {
        if(mShowAnimation) {
            setAnimation(holder.itemView, position)
        }
    }

    /**
     * Shows an animation on the cell
     * Reference: https://stackoverflow.com/questions/26724964/how-to-animate-recyclerview-items-when-they-appear
     */
    private fun setAnimation(view: View, position: Int) {
        if(position > mLastPosition) {
            val animation = AnimationUtils.loadAnimation(view.context, mAnimationType)
            animation.duration = mAnimationDuration
            view.startAnimation(animation)
            mLastPosition = position
        }
    }
}