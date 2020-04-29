package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.groupproject.adapter.FavoritesAdapter
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.database.MovieDao
import com.example.groupproject.database.MovieDatabase
import com.example.groupproject.model.Movie
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.movie_detail_items.*
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment(), FavoritesAdapter.RecyclerViewItemClick, CoroutineScope {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"
    private var sessionId: String = ""
    private var favoritesAdapter: FavoritesAdapter? = null

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var favMovieRecycler: RecyclerView
    private lateinit var listOfFavMovies: List<Movie>
    private lateinit var getSP: SharedPreferences

    private var movieDao: MovieDao? = null
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select, container, false)

        favMovieRecycler = view.findViewById(R.id.favMovieRecycler)
        getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        if (getSP.contains(APP_SESSION)) {
            sessionId = getSP.getString(APP_SESSION, "null")!!
        }

        movieDao = MovieDatabase.getDatabase(context!!).movieDao()

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            favoritesAdapter?.clearAll()
            generateComponent()
        }

        generateComponent()
        return view
    }


    private fun generateComponent() {
        listOfFavMovies = ArrayList()
        favoritesAdapter = activity?.applicationContext?.let {
            FavoritesAdapter(
                listOfFavMovies,
                it.applicationContext,
                itemClickListener = this
            )
        }
        favMovieRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        favMovieRecycler.adapter = favoritesAdapter
        getFavmovieCoroutine()
    }


    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    override fun removeFromFavoritesCoroutine(position: Int, item: Movie) {
        lifecycleScope.launchWhenResumed {
            item.selected = 10
            movieDao?.insert(item)
            Toast.makeText(
                activity?.applicationContext,
                "Removed from favorites",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

//    private fun getFavoriteCorotine() {
//        if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
//            return
//        }
//        getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
//        if (getSP.contains(APP_SESSION)) {
//            sessionId = getSP.getString(APP_SESSION, null)!!
//        }
//        lifecycleScope.launchWhenResumed {
//            swipeRefreshLayout.isRefreshing = true
//
//
//            val favorites = withContext(Dispatchers.IO) {
//                try {
//                    val response = RetrofitMoviesService.getMovieApi()
//                        .getFavoriteCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, sessionId)
//                    if (response.isSuccessful) {
//                        val result = response.body()
//                        if (result != null) {
//                            movieDao?.insertAll(result.results)
//                        }
//                        result
//                    } else {
//                        movieDao?.getAllLiked()
//                    }
//                } catch (e: Exception) {
//                    movieDao?.getAllLiked()
//                }
//            }
//
//            if (favorites == null) {
//                Toast.makeText(activity, "No movie added", Toast.LENGTH_LONG).show()
//            }
//            favoritesAdapter?.listOfFavMovies = favorites as List<*>?
//            favoritesAdapter?.notifyDataSetChanged()
//            swipeRefreshLayout.isRefreshing = false
//        }
//    }

    private fun getFavmovieCoroutine() {
        lifecycleScope.launchWhenResumed {
            swipeRefreshLayout.isRefreshing = true
            val selectedOffline = movieDao?.getLikedOffline(11)
            if (selectedOffline != null) {
                for (i in selectedOffline) {
                    val result = JsonObject().apply {
                        addProperty("media_type", "movie")
                        addProperty("media_id", i)
                        addProperty("favorite", true)
                    }
                    try {
                        RetrofitMoviesService.getMovieApi()
                            .rateCoroutine(sessionId, BuildConfig.MOVIE_DB_API_TOKEN, result)
                    } catch (e: Exception) {
                    }
                }
            }

            val unSelectOffline = movieDao?.getLikedOffline(10)
            if (unSelectOffline != null) {
                for (i in unSelectOffline) {
                    val result = JsonObject().apply {
                        addProperty("media_type", "movie")
                        addProperty("media_id", i)
                        addProperty("favorite", false)
                    }
                    try {
                        RetrofitMoviesService.getMovieApi()
                            .rateCoroutine(sessionId, BuildConfig.MOVIE_DB_API_TOKEN, result)
                    } catch (e: Exception) {
                    }
                }
            }

            val unSelectMoviesOffline = movieDao?.getUnLikedOffline()
            val newArray: ArrayList<Movie>? = null
            if (unSelectMoviesOffline != null) {
                for (movie in unSelectMoviesOffline) {
                    movie.selected = 0
                    newArray?.add(movie)
                }
            }

            if (movieDao != null) newArray?.let { movieDao?.insertAll(it) }

            val list = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitMoviesService.getMovieApi().getFavoriteCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, sessionId)

                    if (response.isSuccessful) {
                        val result = response.body()?.results
                        if (result != null) {
                            for (m in result) {
                                m.selected = 1
                            }
                        }
                        if (!result.isNullOrEmpty()) {
                            movieDao?.insertAll(result)
                        }
                        result
                    } else {
                        movieDao?.getAllLiked() ?: emptyList()
                    }
                } catch (e: Exception) {
                    movieDao?.getAllLiked() ?: emptyList()
                }
            }

            favoritesAdapter?.listOfFavMovies = list
            favoritesAdapter?.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
