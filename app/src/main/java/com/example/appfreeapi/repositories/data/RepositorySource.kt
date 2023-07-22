package com.example.appfreeapi.repositories.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.appfreeapi.repositories.data.model.RepositoriesModel
import com.example.appfreeapi.repositories.data.model.RepositoryModel

class RepositorySource(private val repo: RepositoryRepo, private val query: String): PagingSource<Int, RepositoryModel>() {

    override fun getRefreshKey(state: PagingState<Int, RepositoryModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryModel> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = repo.getRepositories(query, nextPageNumber)

            if(!response.success) return LoadResult.Error(Throwable(response.error.toString()))
            response.data?.let {data->

                return LoadResult.Page(
                    data = data.items,
                    prevKey = null, // Only paging forward.
                    //TODO под вопросом
                    nextKey = if (data.totalCount < RepositoriesModel.perPage || nextPageNumber==100) null else nextPageNumber + 1
                )
            }?: kotlin.run {
                return LoadResult.Error(Throwable("Нет данных"))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}