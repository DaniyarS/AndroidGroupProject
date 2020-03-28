package com.example.groupproject

import com.example.groupproject.GetMoviesResponse
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitMoviesService {
    const val BASE_URL = "https://api.themoviedb.org/3/" //base url of tmdb

    fun getMovieApi(): MovieApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(getOkHttp())
            .build()
        return retrofit.create(MovieApi::class.java)
    }



}

interface MovieApi {

    @GET("movie/popular")  //api to list all popular movies
    fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): Call<GetMoviesResponse>

//    @GET("movie/{id}")
//    fun getMovieById(@Path("id")  id: Int): Call<Movie>
}