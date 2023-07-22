package com.example.appfreeapi.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appfreeapi.App
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProfileVM():ViewModel() {


    private val _sharedFlowEffect = MutableSharedFlow<ProfileEffect>()
    val sharedFlowEffect = _sharedFlowEffect.asSharedFlow()

    fun sendEvent(event:ProfileAction){
        when(event){
            ProfileAction.Exit -> exit()
            ProfileAction.ResetPin -> resetPin()
        }
    }

    private fun exit(){
        viewModelScope.launch {
            _sharedFlowEffect.emit(ProfileEffect.NavToLogin)
        }
    }

    private fun resetPin(){
        viewModelScope.launch {
             App.resetPin()
            _sharedFlowEffect.emit(ProfileEffect.NavToLogin)
        }
    }
}


sealed class ProfileAction(){

    object Exit:ProfileAction()
    object ResetPin:ProfileAction()

}

sealed class ProfileEffect(){

     object NavToLogin:ProfileEffect()
}