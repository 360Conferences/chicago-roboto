package com.chicagoroboto.utils

import android.animation.ObjectAnimator
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.absoluteValue

/**
 * Adapter base class to optionally add animations to the view items
 */
abstract class ChicagoRobotoAdapter<T: RecyclerView.ViewHolder>(private var lastPosition:Int = -1,
                                                            var staggerAnimation:Boolean = false,
                                                            var animationDuration:Long = 300,
                                                            var showAnimation:Boolean = false) :
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

            val oldX = view.x
            view.x = -(view.width + 1000).toFloat()
            val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", oldX)

            // Stagger effect
            if(staggerAnimation) {
                objectAnimator.duration = animationDuration + (lastPosition * 100).absoluteValue
            }

            objectAnimator.start()

            lastPosition = position
        }
    }
}