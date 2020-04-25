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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.groupproject.adapter.FavoritesAdapter
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie
import kotlinx.android.synthetic.main.movie_detail_items.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment(),FavoritesAdapter.RecyclerViewItemClick {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private var session_id: String=""
    private lateinit var getSP : SharedPreferences
    private lateinit var starPreferences: SharedPreferences
    private lateinit var  favMovieRecycler: RecyclerView
    private lateinit var listOfFavMovies: List<Movie>
    private var favoritesAdapter: FavoritesAdapter? = null

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select, container, false)
            favMovieRecycler = view.findViewById(R.id.favMovieRecycler)

        getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        if (getSP.contains(APP_SESSION)){
            session_id = getSP.getString(APP_SESSION,"null")!!
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
        getFavorite()
    }

    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }

    override fun removeFromFavorites(position: Int, item: Movie) {
        lateinit var favoriteRequest: FavoriteRequest
        favoriteRequest= FavoriteRequest("movie",item?.id!!, false)


        RetrofitMoviesService.getMovieApi().addFavorite(BuildConfig.MOVIE_DB_API_TOKEN, session_id, favoriteRequest).enqueue(object: Callback<FavoriteResponse>{
            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {}
            override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                Toast.makeText(activity, "Removed", Toast.LENGTH_SHORT).show()
            }
        })
    }



    @SuppressLint("ShowToast")
    private fun getFavorite(){
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
            if (getSP.contains(APP_SESSION)){
                session_id = getSP.getString(APP_SESSION,null)!!
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getFavorite(BuildConfig.MOVIE_DB_API_TOKEN,session_id).enqueue(object :
                Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("favorite_movies_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        val movieList : GetMoviesResponse? = response.body()
                        if (list?.size==0){
                            Toast.makeText(activity,"No movie added",Toast.LENGTH_LONG).show()
                        }
                        favoritesAdapter?.listOfFavMovies = list
                        favoritesAdapter?.notifyDataSetChanged()

                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
            Toast.makeText(activity?.applicationContext,"No movie added, sign in first",Toast.LENGTH_LONG).show()
        }
    }
}
