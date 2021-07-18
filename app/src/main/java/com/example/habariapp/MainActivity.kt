package com.example.habariapp

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var tooBarConfiguration: Toolbar
    lateinit var drawerLayout: DrawerLayout
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment_cont)

       val appBarConfiguration1 = AppBarConfiguration(setOf(R.id.savedNewsFragment))
        val navHost = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment_cont) as NavHostFragment
        val navController = navHost.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController)




        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigation_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

 /*       actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle!!);
        actionBarDrawerToggle?.syncState();*/



        // bottom_nav.setupWithNavController(nav_host_fragment_cont.findNavController())
        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactoryProvider)
            .get(NewsViewModel::class.java)





    }

  override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                ||super.onSupportNavigateUp()
    }
}