package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment(),FavoritesAdapter.RecyclerViewItemClick, CoroutineScope {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private var sessionId: String=""
    private lateinit var getSP : SharedPreferences
    private lateinit var starPreferences: SharedPreferences
    private lateinit var  favMovieRecycler: RecyclerView
    private lateinit var listOfFavMovies: List<Movie>
    private var favoritesAdapter: FavoritesAdapter? = null

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        if (getSP.contains(APP_SESSION)){
            sessionId = getSP.getString(APP_SESSION,"null")!!
        }
        starPreferences = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            favoritesAdapter?.clearAll()
            generateComponent()
        }
        generateComponent()

        return view
    }

    private fun generateComponent(){
        listOfFavMovies = ArrayList()
        favoritesAdapter =activity?.applicationContext?.let {FavoritesAdapter(listOfFavMovies, it.applicationContext,itemClickListener = this)  }
        favMovieRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        favMovieRecycler.adapter = favoritesAdapter
        getFavoriteCorotine()
    }

    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }

    override fun removeFromFavoritesCoroutine(position: Int, item: Movie) {
        var response: Response<FavoriteResponse>
        val favoriteRequest = item.id?.let { FavoriteRequest("movie", it, false) }
        launch {
            response = favoriteRequest?.let {
                RetrofitMoviesService.getMovieApi()
                    .addFavoriteCoroutine(
                        BuildConfig.MOVIE_DB_API_TOKEN,
                        sessionId,
                        it
                    )
            }!!

            if (response.isSuccessful) {
                Toast.makeText(
                    view?.context,
                    "Removed",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    view?.context,
                    "Internet connection lost",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun getFavoriteCorotine() {
        if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
            return
        }
        getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        if (getSP.contains(APP_SESSION)) {
            sessionId = getSP.getString(APP_SESSION, null)!!
        }
        lifecycleScope.launchWhenResumed {
            swipeRefreshLayout.isRefreshing = true
            val response = RetrofitMoviesService.getMovieApi()
                .getFavoriteCoroutine(BuildConfig.MOVIE_DB_API_TOKEN, sessionId)
            if (response.isSuccessful) {
                val list = response.body()?.results
                if (list?.size == 0) {
                    Toast.makeText(activity, "No movie added", Toast.LENGTH_LONG).show()
                }
                favoritesAdapter?.listOfFavMovies = list
                favoritesAdapter?.notifyDataSetChanged()
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
