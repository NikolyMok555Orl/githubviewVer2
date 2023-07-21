package com.example.appfreeapi.data.model

import com.example.appfreeapi.data.js.JsRepository

data class RepositoriesModel(
    val totalCount: Int,
    val items: List<RepositoryModel>
) {


    companion object {

        const val perPage = 10

    }
}


data class RepositoryModel(
    val id: Int,
    val fullName: String,
    val nameOwner: String,
    val description: String,
    val languages: List<String>,
    val languagesUrl: String,
    val updatedAt: String,
    val avatarUrl: String,
    val stargazersCount: Int
) {


    constructor(jsRepository: JsRepository, languages: List<String>) : this(
        id = jsRepository.id,
        fullName = jsRepository.full_name,
        description = jsRepository.description ?: "",
        updatedAt = jsRepository.updated_at,
        avatarUrl = jsRepository.owner.avatar_url,
        stargazersCount = jsRepository.stargazers_count,
        languagesUrl = jsRepository.languages_url,
        nameOwner = jsRepository.owner.login,
        languages = languages,
    )

    //TODO переделать
    suspend fun getLanguages(): String {
        /* if(languages.isNotEmpty()) return languages.joinToString()
         return try {
             val res=  Resource.success(data = Api.get().getLanguages(url=languages_url).keys.toList())
             languages=res.data?: emptyList()
             languages.joinToString()
         } catch (exception: Exception) {
             "Ошибка при загрузки"
         }*/
        return ""
    }
}