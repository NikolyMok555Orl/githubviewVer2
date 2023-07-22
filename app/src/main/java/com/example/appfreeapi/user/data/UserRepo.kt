package com.example.appfreeapi.user.data

import com.example.appfreeapi.R
import com.example.appfreeapi.data.Api
import com.example.appfreeapi.user.data.model.UserModel
import com.example.appfreeapi.utils.ErrorData
import com.example.appfreeapi.utils.ResponseJs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo {
    suspend fun getUser(userOwner:String): ResponseJs<UserModel, ErrorData> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Api.get().getUser(userOwner)
            }.onSuccess { res ->
                return@withContext ResponseJs(true, UserModel(res), null)
            }.onFailure {
                return@withContext ResponseJs(false, null, ErrorData(text = it.message))
            }
            return@withContext ResponseJs(false, null, ErrorData(resStr = R.string.error_unknow))
    }
}