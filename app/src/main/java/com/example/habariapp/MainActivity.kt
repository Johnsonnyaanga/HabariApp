package com.example.habariapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewpager.widget.ViewPager
import com.example.habariapp.activities.ArticleActivity
import com.example.habariapp.activities.BookMarksActivity
import com.example.habariapp.activities.SettingsActivity
import com.example.habariapp.adapters.ViewPagerAdapter
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var viewModel: NewsViewModel
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor((Color.BLACK))


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.nav_open,
                R.string.nav_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)





        val fm: FragmentManager? = this.supportFragmentManager
        pagerAdapter = ViewPagerAdapter(fm, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter

        // Assigning ViewPager View and setting the adapter
        viewPager = findViewById(R.id.admin_viewpager)
        viewPager?.adapter = pagerAdapter

        val tabLayout = findViewById<TabLayout>(R.id.admin_tablayout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
       // navController = findNavController(R.id.nav_host_fragment_cont)


/*
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigation_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
*/





        // bottom_nav.setupWithNavController(nav_host_fragment_cont.findNavController())
        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactoryProvider)
            .get(NewsViewModel::class.java)





    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
       if (item.itemId == R.id.savedNewsFragment){
           startActivity(Intent(this, BookMarksActivity::class.java))
       }else if(item.itemId == R.id.settings){
           startActivity(Intent(this, SettingsActivity::class.java))
       }else if (item.itemId == R.id.share_app){
           Toast.makeText(this,"i will share the app",Toast.LENGTH_SHORT).show()
       }
        return false
    }


/*
  override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                ||super.onSupportNavigateUp()
    }*/
}


