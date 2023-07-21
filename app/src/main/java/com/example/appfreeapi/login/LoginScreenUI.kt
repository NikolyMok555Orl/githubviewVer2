package com.example.appfreeapi.login

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.appfreeapi.NavHost
import com.example.appfreeapi.R
import com.example.appfreeapi.ui.theme.AppFreeApiTheme
import com.example.appfreeapi.ui.theme.component.LoadingScreenUI


@Composable
fun LoginScreenUI(
    navController: NavController,
    vm: LoginVM = viewModel(factory = LoginVM.provideFactory(owner = LocalSavedStateRegistryOwner.current))
) {
    val context = LocalContext.current
    val state = vm.state.collectAsState()

    LaunchedEffect(key1 = true) {
        vm.sharedFlowEffect.collect { effect ->
            when (effect) {
                LoginEffect.NavToRepo -> {
                    navController.navigate(NavHost.REPOIES)
                }

                is LoginEffect.ShowToast -> {
                    Toast.makeText(
                        context,
                        effect.text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    when (state.value) {
        is LoginState.Enter -> LoginScreenEnterUI(state.value as LoginState.Enter, vm::sendEvent)
        is LoginState.Registration -> LoginScreenRegistrationUI(
            state.value as LoginState.Registration,
            vm::sendEvent
        )

        is LoginState.TemporaryBlocking -> LoginScreenTemporaryBlockingUI(
            state.value as LoginState.TemporaryBlocking
        )

        LoginState.Loading -> {
            LoadingScreenUI()
        }
    }


}


@Composable
fun LoginScreenRegistrationUI(
    state: LoginState.Registration,
    sendEvent: (event: LoginAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(text = "Регистрация")
        val text = if (state.isFirstEntering) {
            "Введете пин код"
        } else {
            "Потвердите пин код"
        }
        Text(text)
        if (state.error != null) {
            Text(
                text = state.error,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
            )
        }

        Pin(if (state.isFirstEntering) state.firstPin else state.confirmationPin)
        PinKeyBoard(sendEvent)
    }
}

@Composable
fun LoginScreenEnterUI(state: LoginState.Enter, sendEvent: (event: LoginAction) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(text = "Введите пинкод")
        if (state.error != null) {
            Text(
                text = state.error,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error)
            )
        }
        Pin(state.pin)
        PinKeyBoard(sendEvent)


    }
}

@Composable
fun LoginScreenTemporaryBlockingUI(
    state: LoginState.TemporaryBlocking,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(25.dp)
    ) {

        Text(text = "Слишком много попыток!.\n Попробуйте позже ${state.timeSec} секунд", textAlign = TextAlign.Center)

    }
}


@Composable
private fun Pin(pin: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        PinHideUI(pin.isNotEmpty())
        PinHideUI(pin.length >= 2)
        PinHideUI(pin.length >= 3)
        PinHideUI(pin.length >= 4)
    }
}

@Composable
private fun PinKeyBoard(sendEvent: (event: LoginAction) -> Unit) {
    Column(modifier = Modifier.width(IntrinsicSize.Min)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonNumberUI(
                "1", { sendEvent(LoginAction.ClickNumber('1')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "2", { sendEvent(LoginAction.ClickNumber('2')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "3",
                { sendEvent(LoginAction.ClickNumber('3')) },
                modifier = Modifier.padding(10.dp)
            )
        }
        Row() {
            ButtonNumberUI(
                "4",
                { sendEvent(LoginAction.ClickNumber('4')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "5",
                { sendEvent(LoginAction.ClickNumber('5')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "6",
                { sendEvent(LoginAction.ClickNumber('6')) },
                modifier = Modifier.padding(10.dp)
            )
        }
        Row() {
            ButtonNumberUI(
                "7",
                { sendEvent(LoginAction.ClickNumber('7')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "8",
                { sendEvent(LoginAction.ClickNumber('8')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonNumberUI(
                "9",
                { sendEvent(LoginAction.ClickNumber('9')) },
                modifier = Modifier.padding(10.dp)
            )
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

            ButtonNumberUI(
                "0",
                { sendEvent(LoginAction.ClickNumber('0')) },
                modifier = Modifier.padding(10.dp)
            )
            ButtonIconUI(
                R.drawable.delete_left,
                { sendEvent(LoginAction.ClickRemoveChar) },
                Modifier.padding(10.dp)
            )
        }
    }
}


@Composable
fun PinHideUI(isFill: Boolean) {
    Spacer(
        modifier =
        Modifier
            .padding(15.dp)
            .clip(CircleShape)
            .size(15.dp)
            .background(color = if (isFill) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
    )
}


@Composable
fun ButtonIconUI(@DrawableRes icon: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {

    Card(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), shape = CircleShape,
        modifier = modifier
            .size(50.dp)
            .clickable {
                onClick()
            }) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.delete_left),
                contentDescription = "Очистить", modifier = Modifier
                    .padding(15.dp)
                    .size(50.dp)
            )
        }
    }
}


@Composable
fun ButtonNumberUI(number: String, onClick: () -> Unit, modifier: Modifier = Modifier) {

    Card(elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), shape = CircleShape,
        modifier = modifier
            .size(50.dp)
            .clickable {
                onClick()
            }) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(number, style = MaterialTheme.typography.titleLarge)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginScreenRegistrationUIPreview() {
    AppFreeApiTheme() {
        LoginScreenRegistrationUI(LoginState.Registration("34"), {})
    }

}

@Preview(showBackground = true)
@Composable
private fun LoginScreenEnterUIPreview() {
    AppFreeApiTheme() {
        LoginScreenEnterUI(LoginState.Enter("4343", error = "Неверный пароль"), {})
    }

}


@Preview(showBackground = true)
@Composable
private fun LoginScreenTemporaryBlockingUIPreview() {
    AppFreeApiTheme() {
        LoginScreenTemporaryBlockingUI(state = LoginState.TemporaryBlocking(25))
    }

}


@Preview(showBackground = true)
@Composable
fun ButtonNumberUIPreview() {
    AppFreeApiTheme() {
        Box(modifier = Modifier.padding(5.dp)) {
            ButtonNumberUI("5", {})
        }

    }
}


