package com.example.habariapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.example.habariapp.util.InternetCheck
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var viewModel: NewsViewModel
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout
    private var mInterstitialAd: InterstitialAd? = null
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
        toolbar.setBackgroundColor((Color.parseColor("#D9000000")))
        toolbar.setTitleTextColor(Color.parseColor("#CCFFFFFF"))

        val internet = InternetCheck(application)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.nav_open,
                R.string.nav_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)


      /*  //admob
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712",
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG", adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })*/





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
           showAds()
           startActivity(Intent(this, BookMarksActivity::class.java))
           overridePendingTransition(0,0)

       }else if(item.itemId == R.id.settings){
          showAds()
           startActivity(Intent(this, SettingsActivity::class.java))
           overridePendingTransition(0,0)

       }else if (item.itemId == R.id.share_app){
           showAds()
           val shareIntent = Intent()
           shareIntent.action = Intent.ACTION_SEND
           shareIntent.type="text/plain"
           shareIntent.putExtra(Intent.EXTRA_TEXT, "app playstore download url coming soon")
           startActivity(Intent.createChooser(shareIntent,"Share_to)"))
           //Toast.makeText(this,"i will share the app",Toast.LENGTH_SHORT).show()
       }
        return false
    }

    fun showAds(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

   /* fun checkInternet(){
        if(isConnected()){
            internet_iko.visibility = VISIBLE
            internet_haiko.visibility = GONE
        }else{
            internet_iko.visibility = GONE
            internet_haiko.visibility = VISIBLE
        }
    }*/


    @Throws(InterruptedException::class, IOException::class)
    fun isConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }


/*
  override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                ||super.onSupportNavigateUp()
    }*/
}


