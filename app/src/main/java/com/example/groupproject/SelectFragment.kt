package com.example.groupproject

import android.annotation.SuppressLint
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
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment() {

    private lateinit var favMovieRecycler: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listOfFavMovies: List<Movie>
    private var favoritesAdapter: FavoritesAdapter? = null
    private lateinit var movieTitle: TextView
    private lateinit var movieImageBackdrop:ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectActivity = inflater.inflate(R.layout.fragment_select, container, false)

            activity?.let {
                val movieId = it.intent.getIntExtra("movie_id", 1)
                movieImageBackdrop = selectActivity.findViewById(R.id.ivMovie)
                movieTitle = selectActivity.findViewById(R.id.tvMovieName)
                favMovieRecycler = selectActivity.findViewById(R.id.favMovieRecycler)
                swipeRefreshLayout = selectActivity.findViewById(R.id.swipeRefreshLayout)
                swipeRefreshLayout.setOnRefreshListener {
                    favoritesAdapter?.clearAll()
                    generate(movieId)
                }
                generate(movieId)
            }
        
        return selectActivity
    }


    fun generate(movieId:Int){
        listOfFavMovies = ArrayList()
        favoritesAdapter =activity?.applicationContext?.let {FavoritesAdapter(listOfFavMovies, it.applicationContext)  }
        favMovieRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        favMovieRecycler.adapter = favoritesAdapter

        addToFavorite(movieId)
    }

    @SuppressLint("ShowToast")
    private fun addToFavorite(movieId:Int){
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getMovieById(movieId,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
                Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<Movie>, response: Response<Movie>
                ) {
                    val post = response.body()
                    if (post != null) {
                        Glide.with(movieImageBackdrop).load(post.getBackDropPathImage()).into(movieImageBackdrop)
                        movieTitle.text = post.title
                        val list = response.body()?.results
                        favoritesAdapter?.listOfFavMovies= list
                        favoritesAdapter?.notifyDataSetChanged()
                    }

                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }
}
