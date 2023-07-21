package com.example.appfreeapi.repositories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.appfreeapi.NavHost
import com.example.appfreeapi.data.model.RepositoryModel
import com.example.appfreeapi.ui.theme.component.ErrorScreenUI
import com.example.appfreeapi.ui.theme.component.LoadingScreenUI
import com.example.appfreeapi.ui.theme.component.SearchTextUI


@Composable
fun SearchRepoScreenUI(
    navController: NavController, searchRepoVM: SearchRepoVM = viewModel(),
    modifier: Modifier = Modifier
) {

    val lazyListState: LazyListState = rememberLazyListState()
    val repoList = searchRepoVM.pagingDataFlow.collectAsLazyPagingItems()
    val stateUI = searchRepoVM.state.collectAsState()
    LaunchedEffect(key1 = true) {
        searchRepoVM.sharedFlowEffect.collect { effect ->
            when (effect) {
                is SearchRepoEffect.NavToUser -> {
                    navController.navigate("${NavHost.USER}/${effect.nameOwner}")
                }
            }
        }
    }


    SearchRepoScreenUI(stateUI.value, repoList, searchRepoVM::sendEvent)


}

@Composable
private fun SearchRepoScreenUI(
    stateUI: SearchStateUI,
    repoList: LazyPagingItems<RepositoryModel>,
    sendEvent: (event: SearchRepoEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SearchTextUI(
            query = stateUI.query,
            onQueryChange = { sendEvent(SearchRepoEvent.ChangeQuery(it)) },
            onSearchClick = {
                sendEvent(SearchRepoEvent.ClickSearch)
            },
            onCloseAndClear = {
                sendEvent(SearchRepoEvent.ClearQuery)
            })


        if (repoList.loadState.refresh is LoadState.Loading && !stateUI.isNotLoading) {
            LoadingScreenUI()
        } else if (repoList.loadState.refresh is LoadState.Error) {
            ErrorScreenUI("Ошибка при загрузки")
        } else if(repoList.loadState.refresh !is LoadState.Loading) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(repoList.itemCount) {
                    repoList[it]?.let {
                        RepoItemUI(
                            repository = it,
                            onClick = { sendEvent(SearchRepoEvent.ClickToCart(it)) })
                    }

                }
            }
        }
    }
}


/* val showButton by remember {
           derivedStateOf {
               lazyListState.firstVisibleItemIndex > 0
           }
       }
       val composableScope = rememberCoroutineScope()
       AnimatedVisibility(visible = showButton) {
           ScrollToTopButton(
               onClick = {
                   composableScope.launch() {
                       // Animate scroll to the first item
                       lazyListState.animateScrollToItem(index = 0)
                   }
               }
           )
       }*/
