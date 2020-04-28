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
import kotlinx.coroutines.*
import retrofit2.Response
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
    private lateinit var movieGenre: TextView
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

    @RequiresApi(Build.VERSION_CODES.M)
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
        movieGenre = findViewById(R.id.textView6)
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

        ivAddList.setOnClickListener() {

            if (loginCount == 0) {
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
                setFragment(authorizationFragment)
            } else {
                if (isInternetAvailable(this)) {
                    progressBar.visibility = View.VISIBLE
                    addToFavoriteCoroutine(movieId)
                } else {
                    progressBar.visibility = View.VISIBLE
                    isFavorite(movieId)
                }
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
                val response = RetrofitMoviesService.getMovieApi()
                    .getMovieByIdCoroutine(id, BuildConfig.MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val post = response.body()
                    if (post != null) {
                        movieDao?.insert(post)
                    }
                    post
                } else {
                    movieDao?.getMovie(id)
                }
            }

            Glide.with(movieImageBackdrop)
                .load("https://image.tmdb.org/t/p/original" + movie?.backdrop_path)
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
            progressBar.visibility = View.GONE
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCreditsCoroutine(id: Int) {
        lifecycleScope.launchWhenResumed {
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
        }
    }

    private fun addToFavoriteCoroutine(item: Int) {
        lateinit var favoriteRequest: FavoriteRequest
        if (ivAddList.drawable.constantState == resources.getDrawable(
                R.drawable.ic_star_border_black_24dp,
                null
            ).constantState
        ) {
            isClicked = true
            favoriteRequest = FavoriteRequest("movie", item, isClicked)
            ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
            launch {
                val response: Response<FavoriteResponse> = RetrofitMoviesService.getMovieApi()
                    .addFavoriteCoroutine(
                        BuildConfig.MOVIE_DB_API_TOKEN,
                        sessionId,
                        favoriteRequest
                    )
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (isClicked) {
                    movie.selected = 11
                    movieDao?.insert(movie)
                }
            }
        } else {
            isClicked = false
            ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)
            favoriteRequest = FavoriteRequest("movie", item, isClicked)
            launch {
                val response: Response<FavoriteResponse> = RetrofitMoviesService.getMovieApi()
                    .addFavoriteCoroutine(
                        BuildConfig.MOVIE_DB_API_TOKEN,
                        sessionId,
                        favoriteRequest
                    )
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!isClicked) {
                    movie.selected = 10
                    movieDao?.insert(movie)
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
                        var select = gson.fromJson(
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        Log.i("msg", result.toString())
        return result
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }
}