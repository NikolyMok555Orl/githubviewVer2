package com.example.appfreeapi.data

import com.example.appfreeapi.repositories.data.js.JsRepositories
import com.example.appfreeapi.user.data.js.JsUser
import com.example.appfreeapi.repositories.data.model.RepositoriesModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit




interface Api {


    @GET("search/repositories")
    suspend fun searchRepositories(@Header("Authorization") token :String =Api.token,
                                   @Query("q") q: String,
                                   @Query("per_page") per_page: Int = RepositoriesModel.perPage,
                                   @Query("page") page: Int
    ): JsRepositories


    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String): JsUser

    @GET
    suspend fun getLanguages(@Header("Authorization") token :String =Api.token ,@Url url: String): Map<String, Int>


    companion object {
        /**Токен временный, для использование может потребовать замены*/
        private const val token="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"
        private const val URL = "https://api.github.com/"

        private var api: Api? = null

        private fun createApi() {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val client: OkHttpClient = OkHttpClient.Builder().apply {
                this.addInterceptor(interceptor)
                this.readTimeout(60, TimeUnit.SECONDS)
                this.connectTimeout(60, TimeUnit.SECONDS)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            api = retrofit.create(Api::class.java)
        }

        fun get(): Api {
            if (api == null)
                createApi()
            return api!!
        }
    }


}