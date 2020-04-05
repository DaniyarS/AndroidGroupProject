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
    lateinit var favlist: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val selectActivity = inflater.inflate(R.layout.fragment_select, container, false)
        movieImageBackdrop = selectActivity.findViewById(R.id.ivMovie)
        movieTitle = selectActivity.findViewById(R.id.tvMovieName)
        favMovieRecycler = selectActivity.findViewById(R.id.favMovieRecycler)
        swipeRefreshLayout = selectActivity.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            favoritesAdapter?.clearAll()
            generate()
        }
        generate()
        return selectActivity
    }


//    private fun generateArray(){
//
//        var listFromMDActivity: MutableList<Int> = MovieDetailActivity().list
//
//        for (idMovie in listFromMDActivity){
//            addToFavorite(listFromMDActivity[idMovie])
//            generate()
//        }
//    }

    private fun generate(){
        listOfFavMovies = ArrayList()
        favoritesAdapter =activity?.applicationContext?.let {FavoritesAdapter(listOfFavMovies, it.applicationContext)  }
        favMovieRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        favMovieRecycler.adapter = favoritesAdapter

        addToFavorite()
    }


    private fun addToFavorite(){
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            swipeRefreshLayout.isRefreshing = true
            //var listFromMDActivity: MutableList<Int> = MovieDetailActivity().list
            for (idMovie in favlist){
                RetrofitMoviesService.getMovieApi().getMovieById(favlist[idMovie],BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
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
                })
                }
            } catch (e: Exception) {
        }
    }
}
