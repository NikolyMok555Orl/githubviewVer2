package com.example.appfreeapi

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.text.isDigitsOnly
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object SecuritySetting {

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        setCrypto(masterKey, sharedPreferences)

    }

    private var masterKey: MasterKey? = null
    private var sharedPreferences: SharedPreferences? = null
    private const val PIN = "PIN"
    private const val SESSION_DURATION = 180000L
    private var timeIsEndSession: Long = 0

    private val _sharedFlowExit = MutableSharedFlow<Boolean>()
    val sharedFlowExit = _sharedFlowExit.asSharedFlow()


    private fun setCrypto(masterKey: MasterKey, sharedPreferences: SharedPreferences) {
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