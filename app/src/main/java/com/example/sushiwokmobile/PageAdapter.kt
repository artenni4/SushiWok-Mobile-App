package com.example.sushiwokmobile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm: FragmentManager, val numOfTabs: Int) : FragmentPagerAdapter(fm) {
    private val activeOrdersTab = TakenOrdersTab()
    private val ordersTab = ActiveOrdersTab()

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> ordersTab
            1 -> activeOrdersTab
            else -> activeOrdersTab
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }


}

