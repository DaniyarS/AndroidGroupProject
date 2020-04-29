package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.database.MovieDao
import com.example.groupproject.database.MovieDatabase
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext


class MovieDetailActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private lateinit var ivAddList: ImageView

    private lateinit var progressBar: ProgressBar
    private lateinit var authorizationFragment: AuthorizationFragment
    private lateinit var movieImageBackdrop: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRealease: TextView
    private lateinit var movieDuration: TextView
    private lateinit var movieDetails: TextView
    private lateinit var movieDirector: TextView
    private lateinit var movieCast: TextView
    private lateinit var btnFavorite: ImageView
    private lateinit var getSP: SharedPreferences
    private lateinit var sessionId: String
    private lateinit var movie: Movie
    private var isClicked = false

    private var movieDao: MovieDao? = null

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_items)

        val sessionPreference = SessionPreference(this)

        ivAddList = findViewById(R.id.ivAddList)
        getSP = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        sessionId = if (getSP.contains(APP_SESSION)) {
            getSP.getString(APP_SESSION, null)!!
        } else {
            sessionPreference.getRealSessionId().toString()
        }

        progressBar = findViewById(R.id.progressBar)
        movieImageBackdrop = findViewById(R.id.ivMovie)
        movieTitle = findViewById(R.id.tvMovieName)
        movieRealease = findViewById(R.id.tvYear)
        movieDuration = findViewById(R.id.tvDuration)
        movieDetails = findViewById(R.id.tvDetailDesc)
        movieDirector = findViewById(R.id.tvDirectorName)
        movieCast = findViewById(R.id.tvCastName)
        btnFavorite = findViewById(R.id.ivAddList)
        authorizationFragment = AuthorizationFragment()
        val movieId = intent.getIntExtra("movie_id", 1)
        movie = intent.extras?.getSerializable("movie") as Movie
        getMovieDetailCoroutine(id = movieId)
        getCreditsCoroutine(id = movieId)

        movieDao = MovieDatabase.getDatabase(this).movieDao()

        var loginCount = sessionPreference.getLoginCount()

        isFavorite(movieId)

        ivAddList.setOnClickListener() {

            if (loginCount == 0) {
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressBar.visibility = View.VISIBLE
                addToFavoriteCoroutine(movieId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("SetTextI18n")
    private fun getMovieDetailCoroutine(id: Int) {
        lifecycleScope.launchWhenResumed {

            val movie = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitMoviesService.getMovieApi()
                        .getMovieByIdCoroutine(id, BuildConfig.MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val post = response.body()
                        if (post?.runtime != null) {
                            movieDao?.updateMovieRuntime(post.runtime, id)
                        }
                        post
                    } else {
                        movieDao?.getMovie(id) ?: Movie()
                    }
                } catch (e: Exception) {
                    movieDao?.getMovie(id) ?: Movie()
                }
            }

            Glide.with(movieImageBackdrop)
                .load(movie?.getBackDropPathImage())
                .into(movieImageBackdrop)

            movieTitle.text = movie?.title

            val realiseDate = movie?.release_date
            movieRealease.text = "(" + realiseDate?.substring(0, 4) + ")"

            val runtime = movie?.runtime
            if (runtime != null) {
                if (runtime > 60) {
                    val runtimeHours = runtime / 60
                    val runtimeMinutes = runtime % 60
                    movieDuration.text =
                        runtimeHours.toString() + "h " + runtimeMinutes.toString() + "min"
                } else {
                    movieDuration.text = "$runtime min"
                }
            }
            movieDetails.text = movie?.overview
            progressBar.visibility = View.GONE
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCreditsCoroutine(id: Int) {
        lifecycleScope.launchWhenResumed {
            try {
                val response: Response<Credits> = RetrofitMoviesService.getMovieApi()
                    .getCreditsCoroutine(id, BuildConfig.MOVIE_DB_API_TOKEN)

                if (response.isSuccessful) {
                    val creditsBody = response.body()
                    if (creditsBody != null) {
                        val crewCointainer = creditsBody.crew
                        for (crew in crewCointainer) {
                            if (crew.getDirectorName() == "Producer") {
                                movieDirector.text = crew.name
                            }
                        }
                        movieCast.text = ""
                        var movieCastCounter = 0
                        val castContainer = creditsBody.cast
                        for (cast in castContainer) {
                            if (movieCastCounter == 3) {
                                break
                            }
                            movieCast.text = movieCast.text.toString() + cast.getCastName() + " "
                            movieCastCounter += 1
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun addToFavoriteCoroutine(item: Int) {
        if (ivAddList.drawable.constantState == resources.getDrawable(
                R.drawable.ic_star_border_black_24dp,
                null
            ).constantState
        ) {
            isClicked = true
            ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
            lateinit var favoriteRequest: FavoriteRequest
            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", item)
                addProperty("favorite", isClicked)
            }
            launch {
                try {
                    favoriteRequest = FavoriteRequest("movie", item, isClicked)
                    RetrofitMoviesService.getMovieApi().addFavoriteCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, sessionId,
                        favoriteRequest)
                } catch (e: Exception) {
                    try {
                        RetrofitMoviesService.getMovieApi()
                            .rateCoroutine(sessionId, BuildConfig.MOVIE_DB_API_TOKEN, body)
                    } catch (e: Exception) { }
                }
                if (isClicked) {
                    movie.selected = 11
                    movieDao?.insert(movie)
                    Toast.makeText(
                        applicationContext,
                        "Added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        } else {
            isClicked = false
            ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)

            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", item)
                addProperty("favorite", isClicked)
            }
            launch {
                lateinit var favoriteRequest: FavoriteRequest
                try {
                    favoriteRequest = FavoriteRequest("movie", item, isClicked)
                    RetrofitMoviesService.getMovieApi().addFavoriteCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, sessionId,
                        favoriteRequest)
                } catch (e: Exception) {
                    RetrofitMoviesService.getMovieApi()
                        .rateCoroutine(sessionId, BuildConfig.MOVIE_DB_API_TOKEN, body)
                }
                if (!isClicked) {
                    movie.selected = 10
                    movieDao?.insert(movie)
                    Toast.makeText(
                        applicationContext,
                        "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun isFavorite(movieId: Int) {
        launch {
            val selectInt = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitMoviesService.getMovieApi()
                        .hasLikeCoroutine(movieId, BuildConfig.MOVIE_DB_API_TOKEN, sessionId)
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val select = gson.fromJson(
                            response.body(),
                            FavoriteResponse::class.java
                        ).favorite
                        if (select) 1
                        else 0
                    } else {
                        movieDao?.getLiked(movie.id) ?: 0
                    }
                } catch (e: Exception) {
                    movieDao?.getLiked(movie.id) ?: 0
                }
            }
            if (selectInt == 1 || selectInt == 11) {
                ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
        }
    }
}