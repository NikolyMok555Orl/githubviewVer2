package com.example.appfreeapi.data.model

import com.example.appfreeapi.data.js.JsRepository
import com.google.gson.annotations.SerializedName

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
    val languagesMain: String?,
    var languages: List<String>,
    val languagesUrl: String,
    val updatedAt: String,
    val avatarUrl: String,
    val stargazersCount: Int,
    val watchers:Int,
    val htmlUrl:String
) {

    constructor(jsRepository: JsRepository, languages: List<String>) : this(
        id = jsRepository.id,
        fullName = jsRepository.fullName,
        description = jsRepository.description ?: "",
        updatedAt = jsRepository.updatedAt,
        avatarUrl = jsRepository.owner.avatar_url,
        stargazersCount = jsRepository.stargazersСount,
        languagesUrl = jsRepository.languagesUrl,
        nameOwner = jsRepository.owner.login,
        languages = languages,
        watchers=jsRepository.watchers,
        htmlUrl=jsRepository.htmlUrl,
        languagesMain=jsRepository.language
    )


    fun getLanguagesAll():String{
       return if(languages.isNotEmpty()){
           languages.joinToString ()

        }else{
            ""
       }

    }
}