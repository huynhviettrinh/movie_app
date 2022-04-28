package com.example.appmovieandroid.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.appmovieandroid.fragment.MovieSeriesFragment
import com.example.appmovieandroid.fragment.RecommendFragment


@Suppress("DEPRECATION")
class ViewPagerAdapter(fragmentManager: FragmentManager, private val numb: Int)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return numb
    }
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> RecommendFragment()
            1 -> MovieSeriesFragment()
            else -> RecommendFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        var title = ""
        title = when(position) {
            0 -> "Recommend"
            1 -> "Movie Series"
            else -> "Recommend"
        }
        return title
    }
}