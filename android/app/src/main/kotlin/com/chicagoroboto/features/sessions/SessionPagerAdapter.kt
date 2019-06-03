package com.chicagoroboto.features.sessions

import android.content.Context
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.text.SimpleDateFormat
import java.util.Locale

internal class SessionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val dates: MutableList<String> = mutableListOf()
    var context: Context? = null

    override fun getCount(): Int = dates.size

    override fun getItem(position: Int): Fragment = makeSessionListView(dates[position])

    override fun getPageTitle(position: Int): CharSequence {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dates[position])
        return DateUtils.formatDateTime(context, date.time, DateUtils.FORMAT_SHOW_DATE)
    }
}