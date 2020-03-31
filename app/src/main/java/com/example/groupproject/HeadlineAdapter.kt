package com.example.groupproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class HeadlineAdapter(private val context: Context) : PagerAdapter(){

    private var layoutInflater:LayoutInflater? = null
    private val images =  arrayOf(
        R.drawable.headline_img1,
        R.drawable.headline_img2,
        R.drawable.headline_img3,
        R.drawable.headline_img4,
        R.drawable.headline_img5
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.headline_movie_items, null)
        val image = v.findViewById<View>(R.id.ivHeadlineMovie) as ImageView
        image.setImageResource(images[position])
        val vp = container as ViewPager
        vp.addView(v, 0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }
}