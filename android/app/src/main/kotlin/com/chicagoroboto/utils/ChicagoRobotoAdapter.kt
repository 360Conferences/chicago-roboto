package com.chicagoroboto.utils

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils

/**
 * Adapter base class to optionally add animations to the view items
 */
abstract class ChicagoRobotoAdapter<T: RecyclerView.ViewHolder>(private var lastPosition:Int = -1,
                                                            var animationType:Int = android.R.anim.slide_in_left,
                                                            var animationDuration:Long = 1000,
                                                            var showAnimation:Boolean = true) :
        RecyclerView.Adapter<T>() {

    /**
     * Play the animation if enabled
     */
    @CallSuper
    override fun onBindViewHolder(holder: T, position: Int) {
        if(showAnimation) {
            setAnimation(holder.itemView, position)
        }
    }

    /**
     * Shows an animation on the cell
     * Reference: https://stackoverflow.com/questions/26724964/how-to-animate-recyclerview-items-when-they-appear
     */
    private fun setAnimation(view: View, position: Int) {
        if(position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(view.context, animationType)
            animation.duration = animationDuration
            view.startAnimation(animation)
            lastPosition = position
        }
    }
}