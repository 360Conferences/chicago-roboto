package com.chicagoroboto.utils

import android.animation.ObjectAnimator
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.absoluteValue

/**
 * Adapter base class to optionally add animations to the view items
 */
abstract class ChicagoRobotoAdapter<T: RecyclerView.ViewHolder>(var staggerAnimation:Boolean = false,
                                                            var animationDuration:Long = 300,
                                                            var showAnimation:Boolean = false) :
        RecyclerView.Adapter<T>() {

    companion object {
        val STAGGER_FACTOR = 100
    }

    /**
     * Play the animation if enabled
     */
    @CallSuper
    override fun onBindViewHolder(holder: T, position: Int) {
        if(showAnimation) {
            // Set invisible, initially
            holder.itemView.visibility = View.INVISIBLE

            // Post so the item has width, which will be used
            holder.itemView.post({
                setAnimation(holder.itemView, position)
            })

        }
    }

    /**
     * Shows an animation on the cell
     */
    private fun setAnimation(view: View, position: Int) {
        // Start the view out of the screen to the left
        view.x = (-view.width).toFloat()
        view.visibility = View.VISIBLE

        // Animate slide to original position
        val objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f)

        // Stagger effect
        if(staggerAnimation) {
            objectAnimator.duration = animationDuration + (position * STAGGER_FACTOR).absoluteValue
        }

        objectAnimator.start()
    }
}