package com.example.groupproject

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object RetrofitService {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getPostApi(): PostApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(getOkHttp())
            .build()
        return retrofit.create(PostApi::class.java)
    }

//    private fun getOkHttp(): OkHttpClient {
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(getLoggingInterceptor())
//        return okHttpClient.build()
//    }
//
//    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
//            override fun log(message: String) {
//                Log.d("OkHttp", message)
//            }
//        }).apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }
}

interface PostApi{
    @GET("movie/popular")
    fun getPostList(@Query("API_KEY") apiKey: String): Call<List<Post>>

    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>
}