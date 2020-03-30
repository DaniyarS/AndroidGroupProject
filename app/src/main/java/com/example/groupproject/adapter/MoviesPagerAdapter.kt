package com.example.groupproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.groupproject.HomeFragment
import com.example.groupproject.R
import com.example.groupproject.model.Movie
import java.lang.reflect.Array.newInstance
import java.net.URLClassLoader.newInstance


//class MoviesPagerAdapter(fragmentManager: FragmentManager, private val movies: ArrayList<Movie>) :
//    FragmentStatePagerAdapter(fragmentManager) {
//
//    // 2
//    override fun getItem(position: Int): Fragment {
//        return HomeFragment.newInstance(movies[position])
//    }
//
//    // 3
//    override fun getCount(): Int {
//        return movies.size
//    }
//}

