package com.example.appfreeapi.login.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.example.appfreeapi.App
import com.example.appfreeapi.R
import com.example.appfreeapi.utils.ErrorData
import com.example.appfreeapi.utils.MessageToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginVM(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val finalTimeMillis: MutableLiveData<Long> = savedStateHandle.getLiveData(END_TM_TIMER)


    private var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Loading)
    var state: StateFlow<LoginState> = _state

    private val _sharedFlowEffect = MutableSharedFlow<LoginEffect>()
    val sharedFlowEffect = _sharedFlowEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            finalTimeMillis.value?.let {
                val startTime = System.currentTimeMillis()
                if ((it - startTime) > 0) {
                    val durationToFinal = (it - startTime) / 1000
                    startTimer(startTime)
                    _state.emit(LoginState.TemporaryBlocking(durationToFinal))
                } else {
                    if (App.isTheFirstStart()) {
                        _state.emit(LoginState.Registration())
                    } else {
                        _state.emit(LoginState.Enter())
                    }
                }
            } ?: kotlin.run {
                if (App.isTheFirstStart()) {
                    _state.emit(LoginState.Registration())
                } else {
                    _state.emit(LoginState.Enter())
                }

            }
        }
    }


    fun sendEvent(event: LoginAction) {
        when (event) {
            is LoginAction.ClickNumber -> clickNumber(event.num)
            LoginAction.ClickRemoveChar -> clickRemoveChar()
        }
    }


    private fun clickNumber(num: Char) {
        viewModelScope.launch {
            when (_state.value) {
                is LoginState.Enter -> {
                    val stateOld = (_state.value as LoginState.Enter)
                    if (stateOld.pin.length + 1 <= 4) {
                        val stateNew = stateOld.copy(pin = stateOld.pin + num)
                        _state.emit(stateNew)
                        checkEnter(stateNew.pin)
                    }
                }

                is LoginState.Registration -> {
                    val stateOld = (_state.value as LoginState.Registration)
                    if (stateOld.isFirstEntering) {
                        val stateNew = stateOld.copy(firstPin = stateOld.firstPin + num)
                        _state.emit(stateNew)
                    } else if (stateOld.isConfirmationEntering) {
                        val stateNew =
                            stateOld.copy(confirmationPin = stateOld.confirmationPin + num)
                        if (stateNew.confirmationPin.length == 4) {
                            registration(stateNew.firstPin, stateNew.confirmationPin)
                        } else {
                            _state.emit(stateNew)
                        }
                    }
                }

                is LoginState.TemporaryBlocking -> {
                    _sharedFlowEffect.emit(LoginEffect.ShowToast(MessageToast(resStr = R.string.blocked)))
                }

                LoginState.Loading -> {}
            }
        }
    }

    private fun clickRemoveChar() {

            viewModelScope.launch {
                when (_state.value) {
                    is LoginState.Enter -> {
                        val stateOld = (_state.value as LoginState.Enter)
                        if (stateOld.pin.isNotEmpty()) {
                            val stateNew =
                                stateOld.copy(pin = stateOld.pin.take(stateOld.pin.length - 1))
                            _state.emit(stateNew)
                        }
                    }

                    is LoginState.Registration -> {
                        val stateOld = (_state.value as LoginState.Registration)
                        if (stateOld.isFirstEntering && stateOld.firstPin.isNotEmpty()) {
                            val stateNew =
                                stateOld.copy(firstPin = stateOld.firstPin.take(stateOld.firstPin.length - 1))
                            _state.emit(stateNew)
                        } else if (stateOld.isConfirmationEntering && stateOld.confirmationPin.isNotEmpty()) {
                            val stateNew = stateOld.copy(
                                confirmationPin = stateOld.confirmationPin.take(stateOld.confirmationPin.length - 1)
                            )
                            _state.emit(stateNew)
                        }
                    }

                    is LoginState.TemporaryBlocking -> {
                        _sharedFlowEffect.emit(LoginEffect.ShowToast(MessageToast(resStr = R.string.blocked)))
                    }

                    LoginState.Loading -> {}
                }
            }
    }

    private fun checkEnter(pin: String) {
        viewModelScope.launch {
            if (pin.length == 4) {
                if (App.login(pin)) {
                    _sharedFlowEffect.emit(LoginEffect.NavToRepo)
                } else {
                    if (_state.value is LoginState.Enter) {
                        val stateOld = _state.value as LoginState.Enter
                        val newState = stateOld.copy(
                            pin = "",
                            countAttempt = stateOld.countAttempt + 1, error =ErrorData(resStr = R.string.pin_wrong)
                        )
                        if (newState.countAttempt < MAX_ATTEMPT) {
                            _state.emit(newState)
                        } else {
                            _state.emit(LoginState.TemporaryBlocking(0))
                            startTimer(DURATION_BLOCK_SECOND )
                        }
                    }
                }
            }
        }

    }



    private fun startTimer(secondsToCount: Long) {
        val millis = TimeUnit.SECONDS.toMillis(secondsToCount)
        finalTimeMillis.value = System.currentTimeMillis() + millis
        object : CountDownTimer(millis, TimeUnit.SECONDS.toMillis(1)) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    _state.emit(LoginState.TemporaryBlocking((millisUntilFinished / 1000)))
                }
            }

            override fun onFinish() {
                viewModelScope.launch {
                    _state.emit(LoginState.Enter())
                }
            }
        }.start()
    }

    private fun registration(firstPin: String, confirmationPin: String) {
        viewModelScope.launch {
            if (firstPin != confirmationPin) {
                _state.emit(LoginState.Registration(error = ErrorData(resStr = R.string.pin_not_match)))
                return@launch
            }
            if (App.setPIN(firstPin)) {
                _sharedFlowEffect.emit(LoginEffect.NavToRepo)
            } else {
                _state.emit(LoginState.Registration(error = ErrorData(resStr =R.string.error_pin)))
            }
        }
    }

    companion object {
        const val MAX_ATTEMPT = 3
        const val END_TM_TIMER = "endTmTimer"
        const val DURATION_BLOCK_SECOND = 30L

        fun provideFactory(
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
                    return LoginVM(handle) as T
                }
            }
    }


}


sealed class LoginState {
    data class Registration(
        val firstPin: String = "",
        val confirmationPin: String = "",
        val error: ErrorData? = null
    ) : LoginState() {
        /**Вход первый*/
        val isFirstEntering
            get() = confirmationPin.isEmpty() && firstPin.length < 4


        val isConfirmationEntering
            get() = firstPin.length == 4

    }

    data class Enter(val pin: String = "", val countAttempt: Int = 0, val error: ErrorData? = null) :
        LoginState()

    data class TemporaryBlocking(val timeSec: Long) : LoginState()

    object Loading: LoginState()

}

sealed class LoginAction {
    data class ClickNumber(val num: Char) : LoginAction()
    object ClickRemoveChar : LoginAction()
}


sealed class LoginEffect {
    object NavToRepo : LoginEffect()
    data class ShowToast(val toast: MessageToast) : LoginEffect(){


    }
}


