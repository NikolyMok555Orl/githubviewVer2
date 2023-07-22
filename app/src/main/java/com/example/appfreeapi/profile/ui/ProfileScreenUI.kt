package com.example.appfreeapi.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.appfreeapi.NavHost
import com.example.appfreeapi.R


@Composable
fun ProfileScreenUI(navController: NavController, vm: ProfileVM= viewModel()) {
    LaunchedEffect(key1 = true){
        vm.sharedFlowEffect.collect{effect->
            when(effect){
                ProfileEffect.NavToLogin ->navController.navigate(NavHost.START){
                    popUpTo(NavHost.START) {
                        inclusive = true
                    }
                }
            }
        }
    }


    ProfileScreenUI(vm::sendEvent)
}


@Composable
fun ProfileScreenUI(sendEvent:(event:ProfileAction)->Unit,) {
    Column(verticalArrangement = Arrangement.Center,modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Button(onClick = { sendEvent(ProfileAction.ResetPin) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.reset_pin))
        }

        Button(onClick = {sendEvent(ProfileAction.Exit) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.exit))
        }
    }

}