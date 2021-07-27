package com.example.habariapp.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.habariapp.R
import com.example.habariapp.database.NewsDatabase
import com.example.habariapp.repository.NewsRepository
import com.example.habariapp.ui.NewsViewModel
import com.example.habariapp.ui.NewsViewModelFactoryProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_book_marks.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel:NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val newsRepository = NewsRepository(NewsDatabase(this))
        val newsViewModelFactoryProvider = NewsViewModelFactoryProvider(application, newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelFactoryProvider).get(NewsViewModel::class.java)

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
            intent.putExtra(Intent.EXTRA_SUBJECT,"")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //privacy policy agreement
        privacy_policy.setOnClickListener{
        startActivity(Intent(this,PrivacyPolicyActivity:class.java))
        }

        //got to facebook profile
        facebook.setOnClickListener{

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
}