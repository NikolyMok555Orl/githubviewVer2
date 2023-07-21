package com.example.appfreeapi.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.appfreeapi.data.model.RepositoriesModel
import com.example.appfreeapi.data.model.RepositoryModel
import com.example.appfreeapi.utils.ResponseJs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryRepo {

    suspend fun getRepositories(q: String, page: Int): ResponseJs<RepositoriesModel, String> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Api.get().searchRepositories(q = q, page = page)
            }.onSuccess { res ->
                val repositoryAll = res.items.map {
                    async {
                        val lan = getLanguages(it.languagesUrl)
                        RepositoryModel(it, lan)
                    }
                }
                repositoryAll.awaitAll()
                return@withContext ResponseJs(
                    true,
                    RepositoriesModel(res.total_count, repositoryAll.map { it.await() }.toList()),
                    null
                )

            }.onFailure {
                return@withContext ResponseJs(false, null, "$it")
            }
            return@withContext ResponseJs(false, null, "Неизвестная ошибка")
        }


    fun getSearchResult(query: String): Flow<PagingData<RepositoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepositorySource(this, query) }
        ).flow
    }

    private suspend fun getLanguages(urlLanguages: String): List<String> {
        var languages = emptyList<String>()
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Api.get().getLanguages(url = urlLanguages)
            }.onSuccess { res ->
                withContext(Dispatchers.Main) {
                    languages = res.keys.toList()
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    languages = emptyList()
                }
            }
        }
        return languages
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }

}