package com.example.appfreeapi.user.data

import com.example.appfreeapi.data.Api
import com.example.appfreeapi.user.data.model.UserModel
import com.example.appfreeapi.utils.ResponseJs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo {
    suspend fun getUser(userOwner:String): ResponseJs<UserModel, String> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Api.get().getUser(userOwner)
            }.onSuccess { res ->
                return@withContext ResponseJs(true, UserModel(res), null)
            }.onFailure {
                return@withContext ResponseJs(false, null, "$it")
            }
            return@withContext ResponseJs(false, null, "Неизвестная ошибка")
    }
}