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


    companion object {
        private var masterKey: MasterKey? = null
        private var sharedPreferences: SharedPreferences? = null
        private const val PIN = "PIN"
        private const val SESSION_DURATION = 180000L
        private var timeIsEndSession: Long = 0

        private val _sharedFlowExit = MutableSharedFlow<Boolean>()
        val sharedFlowExit = _sharedFlowExit.asSharedFlow()


        fun setCrypto(masterKey: MasterKey, sharedPreferences: SharedPreferences) {
            this.masterKey = masterKey
            this.sharedPreferences = sharedPreferences
        }


        fun setPIN(pin: String): Boolean {
            if (pin.length != 4) return false
            if (!pin.isDigitsOnly()) return false
            if (!sharedPreferences?.getString(PIN, "").isNullOrEmpty()) return false
            sharedPreferences?.edit {
                putString(PIN, pin)
                apply()
            }
            return true
        }

        fun resetPin() {
            sharedPreferences?.edit {
                putString(PIN, "")
                apply()
            }
        }

        fun isTheFirstStart() = sharedPreferences?.getString(PIN, "").isNullOrEmpty()

        suspend fun login(pin: String): Boolean {
            if (pin.length != 4) return false
            if (!pin.isDigitsOnly()) return false
            val isLogin = sharedPreferences?.getString(PIN, "") == pin
            if (isLogin) {
                timeIsEndSession = System.currentTimeMillis() + SESSION_DURATION
                MainScope().launch {
                    startTimerExit()
                }

            }
            return isLogin
        }

        fun checkEndSession(): Boolean = System.currentTimeMillis() >= timeIsEndSession


        private suspend fun startTimerExit() {
            delay(SESSION_DURATION)
            _sharedFlowExit.emit(true)
        }

    }
}