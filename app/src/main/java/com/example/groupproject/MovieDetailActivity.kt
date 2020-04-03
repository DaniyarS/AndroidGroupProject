package com.example.groupproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    private lateinit var movieImageBackdrop: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRealease: TextView
    private lateinit var movieDuration: TextView
    private lateinit var movieGenre: TextView
    private lateinit var movieDetails: TextView
    private lateinit var movieDirector: TextView
    private lateinit var movieCast: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_items)

        progressBar = findViewById(R.id.progressBar)

        movieImageBackdrop = findViewById(R.id.ivMovie)
        movieTitle = findViewById(R.id.tvMovieName)
        movieRealease= findViewById(R.id.tvYear)
        movieDuration= findViewById(R.id.tvDuration)
        movieGenre= findViewById(R.id.textView6)
        movieDetails = findViewById(R.id.tvDetailDesc)
        movieDirector= findViewById(R.id.tvDirectorName)
        movieCast= findViewById(R.id.tvCastName)

        val movieId= intent.getIntExtra("movie_id", 1)
        getMovieDetail(id = movieId)
        getCredits(id = movieId)

    }

    private fun getMovieDetail(id: Int) {
        RetrofitMoviesService.getMovieApi().getMovieById(id,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
            @SuppressLint("SetTextI18n")
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
                    movieDuration.text= "$runtime min"
                    }

                    val genreNameContainer = post.genres
                    movieGenre.text=""
                    var genreCounter = 1
                    for (genre in genreNameContainer){
                        if (genreCounter == genreNameContainer.size) {
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()}
                        else{
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()+ " • "}
                        genreCounter += 1
                    }
                    movieDetails.text=post.overview

               }
            }
        })
    }

    private fun getCredits(id: Int) {
        RetrofitMoviesService.getMovieApi().getCredits(id,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object: Callback<Credits> {
            override fun onFailure(call: Call<Credits>, t: Throwable) {
                TODO("Not yet implemented")
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
                val creditsBody = response.body()
                if (creditsBody!=null){
                    val crewCointainer = creditsBody.crew
                    for (crew in crewCointainer){
                        if (crew.getDirectorName() == "Producer"){
                            movieDirector.text = crew.name
                        }
                    }
                    movieCast.text=""
                    var movieCastCounter = 0
                    val castContainer = creditsBody.cast
                    for (cast in castContainer){
                        if (movieCastCounter == 3){
                            break
                        }
                        movieCast.text= movieCast.text.toString() + cast.getCastName() + " "
                        movieCastCounter += 1
                    }
                }
            }
        })
    }
}
