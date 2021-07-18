package com.example.habariapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.example.habariapp.adapters.ViewPagerAdapter
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var tooBarConfiguration: Toolbar
    lateinit var drawerLayout: DrawerLayout
    var viewPager: ViewPager? = null
    var pagerAdapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>("Breaking news", "Technology","Sports","Entertainment","Health")
    var Numboftabs = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm: FragmentManager? = this.supportFragmentManager
        pagerAdapter = ViewPagerAdapter(fm, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter

        // Assigning ViewPager View and setting the adapter
        viewPager = findViewById(R.id.admin_viewpager)
        viewPager?.setAdapter(pagerAdapter)

        val tabLayout = findViewById<TabLayout>(R.id.admin_tablayout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

       // bottom_nav.setupWithNavController(nav_host_fragment_cont.findNavController())
        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this,newsViewModelFactoryProvider)
            .get(NewsViewModel::class.java)


 /*       drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment_cont)
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigation_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)*/


    }

  /*  override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                ||super.onSupportNavigateUp()
    }*/
}