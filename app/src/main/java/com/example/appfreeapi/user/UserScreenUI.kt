package com.example.appfreeapi.user

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appfreeapi.R
import com.example.appfreeapi.data.model.UserModel
import com.example.appfreeapi.ui.theme.AppFreeApiTheme
import com.example.appfreeapi.ui.theme.component.LoadingScreenUI


@Composable
fun UserScreen(navController: NavController, userUrl:String,
               vm:UserVM= viewModel(factory = UserVM.provideFactory(userOwner =userUrl,
                   owner = LocalSavedStateRegistryOwner.current))){

        val state=vm.state.collectAsState()
        when(state.value){
            is UserState.Error -> {}
            UserState.Loading -> LoadingScreenUI()
            is UserState.Success -> {
                UserScreenUI((state.value as UserState.Success).user)

            }
        }
}


@Composable
fun UserScreenUI( user: UserModel, modifier: Modifier = Modifier) {
    val context= LocalContext.current
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(Modifier.size(125.dp), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .padding(5.dp),
                    placeholder = painterResource(R.drawable.ic_person)
                )
                Text(
                    text = "${user.followers}, ${user.following}",
                    modifier.fillMaxSize(),
                    textAlign = TextAlign.Right
                )
            }
            Text(text = user.name, style = MaterialTheme.typography.titleSmall)
            Text(text = user.bio, style = MaterialTheme.typography.bodyMedium)
            if (user.blog.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = {
                        //TODO вынести
                        startActivity(context,
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(user.blog)
                            ), null
                        )
                    }) {
                        Text(
                            text = user.blog,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textDecoration= TextDecoration.Underline
                        )
                        //TODO найти более подходящаю модель
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Ссылка", modifier = Modifier.padding(4.dp), tint= Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun UserScreenUIPreview() {
    AppFreeApiTheme() {
        UserScreenUI(UserModel(id=22032646,
            name = "tidyverse",
            bio = "The tidyverse is a collection of R packages that share common principles and are designed to work together seamlessly",
            avatarUrl = "https://i.playground.ru/i/pix/1484606/image.jpg",
            followers = 794,
            following = 0,
            blog = "http://tidyverse.org",
            htmlUrl="https://github.com/tidyverse",
            type="Organization",
            publicRepos=40,
            createdAt="2016-09-06T16:05:40Z",
            updatedAt="2023-07-13T11:28:32Z",
        )
        )
    }
}

