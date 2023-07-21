package com.example.appfreeapi.utils



data class ResponseJs<T, E>(
    val success: Boolean,
    val data: T?,
    val error: E?,
)