package com.example.appfreeapi

import android.app.Application
import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.core.content.edit
import androidx.core.text.isDigitsOnly
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.appfreeapi.login.ui.LoginEffect
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SecuritySetting.init(this)
    }
}