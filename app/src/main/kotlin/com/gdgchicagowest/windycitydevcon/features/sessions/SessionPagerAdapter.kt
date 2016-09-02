package com.gdgchicagowest.windycitydevcon.features.sessions

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.gdgchicagowest.windycitydevcon.features.sessions.SessionListView
import java.text.SimpleDateFormat

internal class SessionPagerAdapter : PagerAdapter() {

    val format = SimpleDateFormat("yyyy-MM-dd")
    val dates: MutableList<String> = mutableListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = SessionListView(container.context)
        v.setDate(dates[position])
        container.addView(v)
        return v
    }

    override fun getPageTitle(position: Int): CharSequence {
        val date = format.parse(dates[position])
        return format.format(date)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any?): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return dates.size
    }
}