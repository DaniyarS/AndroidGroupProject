package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groupproject.adapter.MoviesAdapter
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.database.MovieDetail
import com.example.groupproject.database.MovieDetailDao
import com.example.groupproject.database.MovieDetailDatabase.Companion.getDatabase
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.coroutines.CoroutineContext


class MovieDetailActivity : AppCompatActivity(), CoroutineScope {

    private val APP_PREFERENCES = "appSettings"
    private val APP_SESSION = "session_id"
    private val STAR_STATE = "starState"


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
    private lateinit var starPreferences: SharedPreferences
    private lateinit var session_id: String
    private var isClicked = false

    private val job = Job()
    private var moviesAdapter: MoviesAdapter? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private var movieDetailDao: MovieDetailDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_items)

        val sessionPreference = SessionPreference(this)

        movieDetailDao = MovieDetailDao.getDatabase(context = this).movieDetailDao

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
            if (loginCount == 0) {
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                addToFavoriteMovie(movieId)
                progressBar.visibility = View.VISIBLE
            }
        }

        getMovieDetailCoroutine()
        getCreditsCoroutine()
        getFavoriteResponse()

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    override fun onResume() {
        super.onResume()
        Log.i("StarStateONPAUSE", getStarState().toString())
        val sessionPreference = SessionPreference(this)
        var loginCount = sessionPreference.getLoginCount()
        val movieId = intent.getIntExtra("movie_id", 1)

        if (getStarState()==false){
            if (loginCount == 0) {
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
                setFragment(authorizationFragment)
            } else {
                addToFavoriteMovie(movieId)
                progressBar.visibility = View.VISIBLE
                setStarState(true)
                print(getStarState().toString())
            }

        } else {
            deletFromFavorite(movieId)
            progressBar.visibility = View.VISIBLE
            setStarState(false)
        }

        ivAddList.setOnClickListener() {
            if (loginCount == 0) {
                Toast.makeText(
                    applicationContext,
                    "Please, sign in first",
                    Toast.LENGTH_SHORT
                ).show()
                setFragment(authorizationFragment)
            } else {
                if (getStarState()==false) {
                    addToFavoriteMovie(movieId)
                    progressBar.visibility = View.VISIBLE
                    setStarState(true)
                    print(getStarState().toString())
                    Toast.makeText(
                        applicationContext,
                        "Added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    deletFromFavorite(movieId)
                    progressBar.visibility = View.VISIBLE
                    setStarState(false)
                    Toast.makeText(
                        applicationContext,
                        "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun addToFavoriteMovie(item: Int) {
        ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
        isClicked = true
        val favoriteRequest = FavoriteRequest("movie", item, isClicked)
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
//                    Toast.makeText(
//                        applicationContext,
//                        "Added to favorites",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })
    }

    private fun deletFromFavorite(item: Int) {
        isClicked = false
        ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)
        val favoriteRequest = FavoriteRequest("movie", item, isClicked)
        RetrofitMoviesService.getMovieApi()
            .addFavorite(BuildConfig.MOVIE_DB_API_TOKEN, session_id, favoriteRequest)
            .enqueue(object : Callback<FavoriteResponse> {

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<FavoriteResponse>,
                    response: Response<FavoriteResponse>
                ) {
                    progressBar.visibility = View.GONE
//                    Toast.makeText(
//                        applicationContext,
//                        "Removed from favorites",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })
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
                        Glide.with(movieImageBackdrop).load("https://image.tmdb.org/t/p/original"+post.backdrop_path)
                            .into(movieImageBackdrop)

                        movieTitle.text = post.title

                        val realiseDate = post.release_date
                        movieRealease.text = "(" + realiseDate?.substring(0, 4) + ")"

                        val runtime = post.runtime
                        if (runtime > 60) {
                            val runtimeHours = runtime / 60
                            val runtimeMinutes = runtime % 60
                            movieDuration.text =
                                runtimeHours.toString() + "h " + runtimeMinutes.toString() + "min"
                        } else {
                            movieDuration.text = "$runtime min"
                        }

//                        val genreNameContainer = post.genres
//                        movieGenre.text = ""
//                        var genreCounter = 1
//                        for (genre in genreNameContainer) {
//                            if (genreCounter == genreNameContainer.size) {
//                                movieGenre.text = movieGenre.text.toString() + genre.getGenreName()
//                            } else {
//                                movieGenre.text =
//                                    movieGenre.text.toString() + genre.getGenreName() + " • "
//                            }
//                            genreCounter += 1
//                        }
//                        movieDetails.text = post.overview

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

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun setStarState(state: Boolean){
        val editor = starPreferences.edit()
        editor.putBoolean(STAR_STATE, state)
        editor.apply()
    }

    private fun getStarState() : Boolean?{
        return starPreferences.getBoolean(STAR_STATE, false)
    }

    private fun setSetMovieId(id: Int){
        val editor = starPreferences.edit()
        editor.putInt(STAR_STATE, id)
        editor.apply()
    }

    private fun getMovieDetailCoroutine() {
        launch {
            progressBar.visibility = View.VISIBLE
            val post = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitMoviesService.getMovieApi().getMovieDetailCoroutine()
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (!result.isNullOrEmpty()) {
                            movieDetailDao?.insertAll(result)
                        }
                        result
                    } else {
                        movieDetailDao?.getAll() ?: emptyList()
                    }
                } catch (e : Exception) {
                    movieDetailDao?.getAll() ?: emptyList<MovieDetail>()
                }
            }
            moviesAdapter?.post = post
            moviesAdapter?.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }
    }
      
    private fun getMovieDetailCoroutine() {
        launch {
            progressBar.visibility = View.VISIBLE
            val response = RetrofitMoviesService.getMovieApi().getMovieDetailCoroutine()
            if (response.isSuccessful) {
                val post = response.body()
                moviesAdapter?.post = post
                moviesAdapter?.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MovieDetailActivity, "Error!", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        }
    }

    private  fun getCreditsCoroutine() {
        launch {
            progressBar.visibility = View.VISIBLE
            val response = RetrofitMoviesService.getMovieApi().getCreditsCoroutine()
            if (response.isSuccessful) {
                val creditsBody = response.body()
                moviesAdapter?.creditsBody = creditsBody
                moviesAdapter?.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MovieDetailActivity, "Error!", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        }
    }

    private  fun getFavoriteResponse() {
        launch {
            progressBar.visibility = View.VISIBLE
            val response = RetrofitMoviesService.getMovieApi().getFavoriteResponseCoroutine()
            if (response.isSuccessful) {
//                val post = response.body()
//                moviesAdapter?.post = post
//                moviesAdapter?.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MovieDetailActivity, "Error!", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
        }
    }

}

