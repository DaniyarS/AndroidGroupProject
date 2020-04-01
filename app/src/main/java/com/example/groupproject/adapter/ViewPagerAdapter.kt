package com.example.groupproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.groupproject.fragment.*


class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val COUNT = 8

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFragment()
            1 -> fragment = MovieFragmentSecond()
            2 -> fragment = MovieFragmentThird()
            3 -> fragment = MovieFragmentFourth()
            4 -> fragment = MovieFragmentFifth()
            5 -> fragment = MovieFragmentSix()
            6 -> fragment = MovieFragmentSeven()
            7 -> fragment = MovieFragmentEigth()
        }

        return fragment!!
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab " + (position + 1)
    }
}
