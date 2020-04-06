package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.groupproject.adapter.MoviesAdapter
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class SelectFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter?=null
    private lateinit var listMovies: List<Movie>

    private var session_id: String=""
    private lateinit var getSP : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select,container,false)
        recyclerView = view.findViewById(R.id.recyclerViewFav)
        generateComponent()
        return view
    }

    private fun generateComponent(){
        listMovies = ArrayList()
        moviesAdapter =activity?.applicationContext?.let {MoviesAdapter(listMovies,it)  }
        recyclerView.layoutManager =   LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = moviesAdapter
        getFavMovies()
    }

    private fun getFavMovies(){
        getSP = activity?.getSharedPreferences("shared_preferences",Context.MODE_PRIVATE)!!
        if (getSP.contains("session_id")){
            session_id = getSP.getString("session_id","null") as String
        }
        RetrofitMoviesService.getMovieApi().getFavorite(BuildConfig.MOVIE_DB_API_TOKEN,session_id).enqueue(object:
            Callback<GetMoviesResponse> {
            override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
//                swipeRefreshLayout.isRefreshing = false
            }
            override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>) {
                if(response.isSuccessful){
                    val movies: GetMoviesResponse? = response.body()
                    if(movies?.results?.size==0){
//                        swipeRefreshLayout.isRefreshing=false
                    }
                    else {
                        moviesAdapter?.ListOfMovies = movies?.results


                        }
                        moviesAdapter?.notifyDataSetChanged()
                    }
                }

        })
    }

}
