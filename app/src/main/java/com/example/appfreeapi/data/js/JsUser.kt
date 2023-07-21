package com.example.appfreeapi.data.js

import com.google.gson.annotations.SerializedName


data class JsUser(
    val id: Int=0,
    val login: String="",
    val bio: String? ="",
    @SerializedName("avatar_url")
    val avatarUrl: String="",
    val blog: String?="",
    val followers:Int=0,
    val following: Int=0,
    @SerializedName("html_url")
    val htmlUrl:String="",
    val type:String="",
    @SerializedName("public_repos")
    val publicRepos:Int,
    @SerializedName("created_at")
    val createdAt:String,
    @SerializedName("updated_at")
    val updatedAt:String
)
