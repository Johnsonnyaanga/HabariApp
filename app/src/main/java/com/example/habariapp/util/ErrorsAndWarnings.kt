package com.example.habariapp.util

import android.content.Context
import android.widget.Toast

class ErrorsAndWarnings( val context: Context) {

    fun toastMessageLong(message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    fun toastMessageShort(message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

}