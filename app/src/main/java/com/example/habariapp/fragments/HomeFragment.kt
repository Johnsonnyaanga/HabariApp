package com.example.habariapp.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.example.habariapp.R
import com.example.habariapp.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment(R.layout.fragment_home) {
    var viewPager: ViewPager? = null
    var pagerAdapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>(
        "Breaking news",
        "Technology",
        "Sports",
        "Entertainment",
        "Health"
    )
    var Numboftabs = 5


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val fm: FragmentManager? = requireActivity().supportFragmentManager
        pagerAdapter = ViewPagerAdapter(fm, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter

        // Assigning ViewPager View and setting the adapter
        viewPager = view.findViewById(R.id.admin_viewpager)
        viewPager?.adapter = pagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.admin_tablayout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }


}