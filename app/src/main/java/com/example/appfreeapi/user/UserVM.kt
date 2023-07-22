package com.example.appfreeapi.user

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner

import com.example.appfreeapi.user.data.model.UserModel
import com.example.appfreeapi.user.data.UserRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserVM(private val userRepo: UserRepo = UserRepo(), private val userOwner: String) :
    ViewModel() {


    private val _state: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
    val state: StateFlow<UserState> = _state

    private val _sharedFlowEffect = MutableSharedFlow<UserEffect>()
    val sharedFlowEffect = _sharedFlowEffect.asSharedFlow()

    init {
        getUser()

    }

    private fun getUser() {
        viewModelScope.launch {
            val res = userRepo.getUser(userOwner)
            if (res.success && res.data != null) {
                _state.emit(UserState.Success(res.data))
            } else {
                res.error?.let {
                    _state.emit(UserState.Error(it))
                } ?: kotlin.run {
                    _state.emit(UserState.Error("Пользователя нет $userOwner"))
                }
            }
        }
    }
    companion object {

        fun provideFactory(
            userRepo: UserRepo = UserRepo(), userOwner: String,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return UserVM(userRepo, userOwner) as T
                }
            }
    }


    fun sendEvent(event:UserAction){
        viewModelScope.launch {
            when (event) {
                is UserAction.OpenBrowser ->{
                    _sharedFlowEffect.emit(UserEffect.OpenBrowser(event.url))

                }
            }
        }
    }

}

sealed class UserState() {

    data class Success(val user: UserModel) : UserState()
    object Loading : UserState()
    data class Error(val error: String) : UserState()
}

sealed class UserAction(){

    data class OpenBrowser(val url:String):UserAction()

}


sealed class UserEffect(){

    data class OpenBrowser(val url:String):UserEffect()
}