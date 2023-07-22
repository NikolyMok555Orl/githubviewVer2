package com.example.appfreeapi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.appfreeapi.R

fun openBrowser(context:Context, url:String){
    ContextCompat.startActivity(
        context,
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ), null
    )
}

interface GetStringDiffResource{
    fun getMessage(context: Context):String


}


data class MessageToast(private val text:String?=null, @StringRes private val resStr:Int?=null):GetStringDiffResource{

        override fun getMessage(context: Context):String{
            if(text!=null){
                return text
            }
            if (resStr!=null){
                return context.getString(resStr)
            }
            return ""
        }
}


data class ErrorData(private val text:String?=null, @StringRes private val resStr:Int?=null, private val additionalInfo:String?=null):GetStringDiffResource{

    override fun getMessage(context: Context):String{
        if(text!=null){
            return "$text: $additionalInfo"
        }
        if (resStr!=null){
            return "${context.getString(resStr)} $additionalInfo"
        }
        return context.getString(R.string.error_unknow)
    }

    override fun toString(): String {
        return text?:"${resStr?: R.string.error_unknow}"
    }
}