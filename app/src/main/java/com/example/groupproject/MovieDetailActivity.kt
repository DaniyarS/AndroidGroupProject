package com.example.groupproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.http.Body
import java.util.ArrayList


class MovieDetailActivity : AppCompatActivity() {


    private lateinit var progressBar: ProgressBar
    private lateinit var movieImageBackdrop: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRealease: TextView
    private lateinit var movieDuration: TextView
    private lateinit var movieAdult: TextView
    private lateinit var movieGenre: TextView
    private lateinit var movieDetails: TextView
    private lateinit var movieDirector: TextView
    private lateinit var movieCast: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        progressBar = findViewById(R.id.progressBar)
        movieImageBackdrop = findViewById(R.id.backdrop_image)
        movieTitle = findViewById(R.id.movie_title)
        movieRealease= findViewById(R.id.release_date)
        movieDuration= findViewById(R.id.time_duration)
        movieAdult = findViewById(R.id.movie_adult)
        movieGenre= findViewById(R.id.genre_film)
        movieDetails = findViewById(R.id.movie_detail)

//        movieDirector= findViewById(R.id.director_film)
//        movieCast= findViewById(R.id.movie_cast)

        val movieId= intent.getIntExtra("movie_id", 1)
        getMovieDetail(id = movieId)

    }

    private fun getMovieDetail(id: Int) {
        RetrofitMoviesService.getMovieApi().getMovieById(id,BuildConfig.MOVIE_DB_API_TOKEN_CREDITS).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                progressBar.visibility = View.GONE
                val post = response.body()
                if (post != null) {
                    Glide.with(movieImageBackdrop).load(post.getBackDropPathImage()).into(movieImageBackdrop)

                    movieTitle.text = post.title

                    val realeaseDate = post.release_date
                    movieRealease.text = "(" + realeaseDate.substring(0,4) + ")"

                    val runtime = post.runtime
                    if (runtime>60) {
                        val runtimeHours = runtime/60
                        val runtimeMinutes = runtime%60
                        movieDuration.text=runtimeHours.toString()+"h " + runtimeMinutes.toString() + "min"
                    }
                    else {
                    movieDuration.text= runtime.toString() + " min"}

                    val adult = post.adult
                    if (adult==true){
                        movieAdult.text="R"}
                    else {
                        movieAdult.text="PG"}

                    val genreNameContainer = post.genres
                    movieGenre.text=""
                    var genreCounter = 1
                    for (genre in genreNameContainer){
                        if (genreCounter == genreNameContainer.size) {
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()}
                        else{
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()+ " • "}
                        genreCounter=genreCounter+1
                    }

                    movieDetails.text=post.overview

                    val direcorAndCastObject = post.credits
                    val crewCointainer = direcorAndCastObject.crew
                    for (crew in crewCointainer){
                        if (crew.getDirectorName().equals("Producer")){
                            movieDirector.text = crew.getDirectorName()
                        }
                    }
                    movieCast.text=""
                    val castContainer = direcorAndCastObject.cast
                    for (cast in castContainer){
                        movieCast.text=movieCast.text.toString() + cast.getCastName()
                    }

               }
            }
        })
    }
}
