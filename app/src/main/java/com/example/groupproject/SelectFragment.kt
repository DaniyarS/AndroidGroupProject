package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
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
import com.bumptech.glide.Glide
import com.example.groupproject.adapter.FavoritesAdapter
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment() {

    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private var session_id: String=""
    private lateinit var getSP : SharedPreferences

    lateinit var  favMovieRecycler: RecyclerView
    private lateinit var listOfFavMovies: List<Movie>
    private var favoritesAdapter: FavoritesAdapter? = null
//    private var movieName: TextView? = null
//    private var movieImage: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select, container, false)
//            movieImage = view?.findViewById(R.id.ivMovieFav)
//            movieName = view?.findViewById(R.id.tvMovieNameFav)
            favMovieRecycler = view.findViewById(R.id.favMovieRecycler)
            generateComponent()

            return view
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


    private fun generateComponent(){
        listOfFavMovies = ArrayList()
        favoritesAdapter =activity?.applicationContext?.let {FavoritesAdapter(listOfFavMovies, it.applicationContext)  }
        favMovieRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        favMovieRecycler.adapter = favoritesAdapter

        getFavorite()
    }


    @SuppressLint("ShowToast")
    private fun getFavorite(){
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return
            }
            getSP = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
            if (getSP.contains(APP_SESSION)){
                session_id = getSP.getString(APP_SESSION,"NULL")!!
            }
            RetrofitMoviesService.getMovieApi().getFavorite(BuildConfig.MOVIE_DB_API_TOKEN,session_id).enqueue(object :
                Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("favorite_movies_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        val movieList : GetMoviesResponse? = response.body()
                        if (list?.size==0){
                            Toast.makeText(activity,"NO FILMS ADDED",Toast.LENGTH_LONG).show()
                        }
                        favoritesAdapter?.listOfFavMovies = list
                        favoritesAdapter?.notifyDataSetChanged()
                    }
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }
}
