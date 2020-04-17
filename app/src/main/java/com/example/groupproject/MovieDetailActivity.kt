package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext


class MovieDetailActivity : AppCompatActivity() {
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
    private lateinit var session_id: String
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_items)

        val sessionPreference = SessionPreference(this)

        ivAddList = findViewById(R.id.ivAddList)
        getSP = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        if (getSP.contains(APP_SESSION)) {
            session_id = getSP.getString(APP_SESSION, null)!!
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
        getMovieDetail(id = movieId)
        getCredits(id = movieId)


        var loginCount = sessionPreference.getLoginCount()

        ivAddList.setOnClickListener() {
            if (loginCount == 0){
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
                setFragment(authorizationFragment)
            }
            else{
            addToFavoriteMovie(movieId)
                progressBar.visibility = View.VISIBLE
            }
        }

    }


    private fun addToFavoriteMovie(item: Int){
        lateinit var favoriteRequest: FavoriteRequest

        if (ivAddList.drawable.constantState == resources.getDrawable(
                R.drawable.ic_star_border_black_24dp,
                null
            ).constantState
        ) {
            ivAddList.setImageResource(R.drawable.ic_star_fav_24dp)
            isClicked = true
            favoriteRequest = FavoriteRequest("movie", item, isClicked)
            ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
            RetrofitMoviesService.getMovieApi()
                .addFavorite(BuildConfig.MOVIE_DB_API_TOKEN, session_id, favoriteRequest)
                .enqueue(object : Callback<FavoriteResponse> {

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Please, sign in first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            isClicked = false
            ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)
            favoriteRequest = FavoriteRequest("movie", item, isClicked)
            RetrofitMoviesService.getMovieApi()
                .addFavorite(BuildConfig.MOVIE_DB_API_TOKEN, session_id, favoriteRequest)
                .enqueue(object : Callback<FavoriteResponse> {

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE}

                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        }

    }

    private fun getMovieDetail(id: Int) {
        RetrofitMoviesService.getMovieApi().getMovieById(id, BuildConfig.MOVIE_DB_API_TOKEN)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    progressBar.visibility = View.GONE
                    val post = response.body()
                    if (post != null) {
                        Glide.with(movieImageBackdrop).load(post.getBackDropPathImage())
                            .into(movieImageBackdrop)

                        movieTitle.text = post.title

                        val realiseDate = post.release_date
                        movieRealease.text = "(" + realiseDate.substring(0, 4) + ")"

                        val runtime = post.runtime
                        if (runtime > 60) {
                            val runtimeHours = runtime / 60
                            val runtimeMinutes = runtime % 60
                            movieDuration.text =
                                runtimeHours.toString() + "h " + runtimeMinutes.toString() + "min"
                        } else {
                            movieDuration.text = "$runtime min"
                        }

                        val genreNameContainer = post.genres
                        movieGenre.text = ""
                        var genreCounter = 1
                        for (genre in genreNameContainer) {
                            if (genreCounter == genreNameContainer.size) {
                                movieGenre.text = movieGenre.text.toString() + genre.getGenreName()
                            } else {
                                movieGenre.text =
                                    movieGenre.text.toString() + genre.getGenreName() + " • "
                            }
                            genreCounter += 1
                        }
                        movieDetails.text = post.overview

                    }
                }
            })
    }

    private fun getCredits(id: Int) {
        RetrofitMoviesService.getMovieApi().getCredits(id, BuildConfig.MOVIE_DB_API_TOKEN)
            .enqueue(object : Callback<Credits> {
                override fun onFailure(call: Call<Credits>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Detail body is not filled yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
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
            })
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }
}