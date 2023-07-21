package com.example.appfreeapi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun openBrowser(context:Context, url:String){
    ContextCompat.startActivity(
        context,
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ), null
    )
}