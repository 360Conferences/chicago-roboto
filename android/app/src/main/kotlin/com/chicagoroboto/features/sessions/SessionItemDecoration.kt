package com.chicagoroboto.features.sessions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chicagoroboto.R

internal class SessionItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    private val divider: Drawable?
    private val spacing: Int

    init {
        spacing = context.resources.getDimensionPixelSize(R.dimen.recycler_spacing_vertical)

        val a = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) return

        val childCount = parent.childCount
        for (i in 0..childCount) {
            val view = parent.getChildAt(i)
            if (view != null) {
                val holder = parent.getChildViewHolder(view)
                if (holder is SessionAdapter.ViewHolder && holder.adapterPosition != 0) {
                    if (holder.binding.timeslot.visibility == View.VISIBLE) {
                        val left = 0
                        val top = holder.itemView.top
                        val right = parent.width
                        val bottom = top + divider.intrinsicHeight
                        divider.setBounds(left, top, right, bottom)
                        divider.draw(c)
                    }
                }
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (divider == null) return

        val holder = parent.getChildViewHolder(view)
        if (holder is SessionAdapter.ViewHolder) {
            if (holder.binding.timeslot.visibility == View.VISIBLE && holder.adapterPosition != 0) {
                outRect.set(0, divider.intrinsicHeight, 0, 0)
            }
        }
    }
}
