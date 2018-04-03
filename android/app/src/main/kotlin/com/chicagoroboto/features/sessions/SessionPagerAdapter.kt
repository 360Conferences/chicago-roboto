package com.chicagoroboto.features.sessions

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.Locale

internal class SessionPagerAdapter : PagerAdapter() {

    val dates: MutableList<String> = mutableListOf()
    var context: Context? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        context = container.context
        val v = SessionListView(container.context)
        v.setDate(dates[position])
        container.addView(v)
        return v
    }

    override fun getPageTitle(position: Int): CharSequence {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dates[position])
        return DateUtils.formatDateTime(context, date.time, DateUtils.FORMAT_SHOW_DATE)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return dates.size
    }
}