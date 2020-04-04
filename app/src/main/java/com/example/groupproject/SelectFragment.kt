package com.example.groupproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.groupproject.adapter.MoviesAdapter
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
    private lateinit var listOfFavMovie: List<Movie>
    private lateinit var movieImageBackdrop: ImageView
    private lateinit var movieTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            favMovieRecycler = it.findViewById(R.id.favMovieRecycler)
            swipeRefreshLayout = it.findViewById(R.id.swipeRefreshLayout)

            swipeRefreshLayout.setOnRefreshListener {
                favMovieRecycler.clearOnChildAttachStateChangeListeners()
            }

        }

        return inflater.inflate(R.layout.fragment_select, container, false)
    }

    fun addToFavorite(movieId:Int){
        RetrofitMoviesService.getMovieApi().getMovieById(movieId,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
            Callback<Movie> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val post = response.body()
                if (post != null) {
                    Glide.with(movieImageBackdrop).load(post.getBackDropPathImage()).into(movieImageBackdrop)
                    movieTitle.text = post.title
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        Toast.makeText(activity?.applicationContext, "Added to Favorites", Toast.LENGTH_SHORT).show()
    }

}
