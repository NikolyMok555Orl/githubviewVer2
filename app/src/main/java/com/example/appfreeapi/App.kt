package com.example.appfreeapi

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.text.isDigitsOnly
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class App:Application() {


    override fun onCreate() {
       super.onCreate()
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        setCrypto(masterKey, sharedPreferences)


    }



    companion object{
        private var masterKey: MasterKey?=null
        private var sharedPreferences:  SharedPreferences?=null
        private const val PIN="PIN"

        fun setCrypto(masterKey: MasterKey,  sharedPreferences: SharedPreferences){
            this.masterKey=masterKey
            this.sharedPreferences=sharedPreferences
        }


        fun setPIN(pin:String):Boolean{
            if(pin.length!=4) return false
            if(!pin.isDigitsOnly()) return false
            if(!sharedPreferences?.getString(PIN, "").isNullOrEmpty()) return false
            sharedPreferences?.edit {
                putString(PIN, pin)
                apply()
            }
            return true
        }
        fun resetPin(){
            sharedPreferences?.edit {
                putString(PIN, "")
                apply()
            }
        }

        fun isTheFirstStart()=sharedPreferences?.getString(PIN, "").isNullOrEmpty()

        fun checkPin(pin:String):Boolean{
            if(pin.length!=4) return false
            if(!pin.isDigitsOnly()) return false
           return sharedPreferences?.getString(PIN, "")==pin
        }

    }
}