package com.example.appfreeapi.user.data.model

import com.example.appfreeapi.user.data.js.JsUser


data class UserModel(
    val id: Int,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val blog: String,
    val followers: Int,
    val following: Int,
    val htmlUrl: String,
    val type: String,
    val publicRepos: Int,
    val createdAt: String,
    val updatedAt: String
) {


    constructor(jsUser: JsUser) : this(
        id = jsUser.id,
        name = jsUser.login,
        bio = jsUser.bio ?: "",
        blog = jsUser.blog ?: "",
        followers = jsUser.followers,
        following = jsUser.following,
        avatarUrl = jsUser.avatarUrl,
        htmlUrl = jsUser.htmlUrl,
        type = jsUser.type,
        publicRepos = jsUser.publicRepos,
        createdAt = jsUser.createdAt,
        updatedAt = jsUser.updatedAt,
    )
}