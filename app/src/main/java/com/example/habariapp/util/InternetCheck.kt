package com.example.habariapp.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.example.habariapp.HabariApplication

class InternetCheck(application: Application) :AndroidViewModel(application) {

    fun hasInternetConnection():Boolean{
        val conectivityManager = getApplication<HabariApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetWork = conectivityManager.activeNetwork ?: return false
            val capabilities =  conectivityManager.getNetworkCapabilities(activeNetWork) ?: return false

            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }



        }else{

            conectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI ->  true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }

        }




        return false
    }
}