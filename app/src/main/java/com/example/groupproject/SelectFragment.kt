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

    private var favMovieRecycler: RecyclerView? = null
    private lateinit var listOfFavMovies: List<Movie>
    private var favoritesAdapter: FavoritesAdapter? = null
    private var movieName: TextView? = null
    private var movieImage: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            val movieId = arguments?.getInt("movie_id")
            movieImage = view?.findViewById(R.id.ivMovieFav)
            movieName = view?.findViewById(R.id.tvMovieNameFav)
            favMovieRecycler = view?.findViewById(R.id.favMovieRecycler)
            Toast.makeText(activity?.applicationContext, movieId.toString(), Toast.LENGTH_SHORT).show()
            if (movieId != null) {
                generate(245891)
            }
    }
        return inflater.inflate(R.layout.fragment_select, container, false)
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


    private fun generate(movieId:Int){
        listOfFavMovies = ArrayList()
        favoritesAdapter =activity?.applicationContext?.let {FavoritesAdapter(listOfFavMovies, it.applicationContext)  }
        favMovieRecycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        favMovieRecycler?.adapter = favoritesAdapter

        addToFavorite(245891)
    }


    @SuppressLint("ShowToast")
    private fun addToFavorite(idMovie:Int){
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            RetrofitMoviesService.getMovieApi().getMovieById(idMovie, BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
                Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                }
                override fun onResponse(call: Call<Movie>, response: Response<Movie>
                ) {
                    Log.d("top_rated_movie_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        favoritesAdapter?.listOfFavMovies = list
                        favoritesAdapter?.notifyDataSetChanged()
                    }
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }
}
