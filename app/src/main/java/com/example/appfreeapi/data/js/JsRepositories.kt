package com.example.appfreeapi.data.js

import com.google.gson.annotations.SerializedName

class JsRepositories {
    var total_count=0
    var items: List<JsRepository> = emptyList()
}


data class JsRepository(
    val id:Int,
    @SerializedName("full_name")
    val fullName:String,
    val name:String,
    val description:String?="",
    val language:String?,
    @SerializedName("languages_url")
    val languagesUrl:String,
    @SerializedName("updated_at")
    val updatedAt:String,
    @SerializedName("stargazers_count")
    val stargazersСount:Int,
    val owner:JsOwner,
    //новое
    val watchers:Int,
    @SerializedName("html_url")
    val htmlUrl:String
)