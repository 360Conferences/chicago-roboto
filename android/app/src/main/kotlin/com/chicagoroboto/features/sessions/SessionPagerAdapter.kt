package com.chicagoroboto.features.sessions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chicagoroboto.features.sessions.SessionDatePresenter.Model.SessionDate
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

internal class SessionPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

  val dates: MutableList<SessionDate> = mutableListOf()

  override fun getItemCount(): Int = dates.size

  override fun createFragment(position: Int): Fragment =
      SessionListFragment.create(dates[position].id)

  fun mediateTabs(tabLayout: TabLayout, viewPager: ViewPager2) {
    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      tab.text = dates[position].name
    }.attach()
  }
}
