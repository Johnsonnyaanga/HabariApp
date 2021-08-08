package com.example.habariapp.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.habariapp.R
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_book_marks.*
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel:NewsViewModel
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        back_settings.setOnClickListener(View.OnClickListener {
            finish()
        })

        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactoryProvider).get(NewsViewModel::class.java)


        //admob
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
        })


         //callback handlers
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
               // Log.d(TAG, 'Ad was dismissed.')
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                //Log.d(TAG, 'Ad failed to show.')
            }

            override fun onAdShowedFullScreenContent() {
                //Log.d(TAG, 'Ad showed fullscreen content.')
                mInterstitialAd = null;
            }
        }






        //delete cached data
        clear_cache.setOnClickListener{
            clearCache()
        }

        //change size of article
        article_size.setOnClickListener{

        }

        //email to the developer
        feedback.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:johnsonyaanga@gmail.com")

            intent.putExtra(Intent.EXTRA_EMAIL, "")
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //privacy policy agreement
        privacy_policy.setOnClickListener{
        startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        //got to facebook profile
        linkedin.setOnClickListener{
            openLinkedInPage("johnson-nyaanga-8349b7174")
        }
        //go to twitter profile
        twitter.setOnClickListener{
            var intent: Intent? = null
            try {
                // get the Twitter app if possible
                this.packageManager.getPackageInfo(
                    "com.twitter.android",
                    0
                )
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=johnsonyaanga")
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } catch (e: Exception) {
                // no Twitter app, revert to browser
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/USERID_OR_PROFILENAME")
                )
            }
            this.startActivity(intent)

        }

        //go to instagram profile
        instagram.setOnClickListener{
            val uri = Uri.parse("http://instagram.com/johnson_nyaanga")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/johnson_nyaanga")
                    )
                )
            }

        }
    }

    override fun onStart() {
        super.onStart()
    }
    fun showAds(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun clearCache(){
        val builder = AlertDialog.Builder(this@SettingsActivity)
        builder.setMessage("Clear cache ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.deleteAllArticles()
                Snackbar.make(parento, "Cleared succesifully", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
    fun openLinkedInPage(linkedId: String) {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@$linkedId"))
        val packageManager: PackageManager = packageManager
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.isEmpty()) {
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.linkedin.com/profile/view?id=$linkedId")
            )
        }
        startActivity(intent)
    }


}