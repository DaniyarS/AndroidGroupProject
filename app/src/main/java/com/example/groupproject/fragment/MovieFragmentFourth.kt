package com.example.groupproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.groupproject.BuildConfig
import com.example.groupproject.R
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.database.MovieDao
import com.example.groupproject.database.MovieDatabase
import com.example.groupproject.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MovieFragmentFourth : Fragment(), CoroutineScope {

    private lateinit var progressBar: ProgressBar
    private lateinit var Image : ImageView
    private lateinit var MovieName: TextView
    private lateinit var MovieGenre: TextView
    private lateinit var MovieIndex: TextView

    //new val job
    private val job = Job()

    private var movieDao : MovieDao?=null

    //override fun for coroutine context
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.headline_movie_items, container,false)

        movieDao = MovieDatabase.getDatabase(context!!).movieDao()

        progressBar = view.findViewById(R.id.progressBar)
        Image = view.findViewById(R.id.ivHeadlineMovie)
        MovieName = view.findViewById(R.id.tvMovieName)
        MovieGenre = view.findViewById(R.id.tvGenre)
        MovieIndex = view.findViewById(R.id.tvImageIndex)
        MovieIndex.text="4"
        getBriefMovieDetail(440)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun getBriefMovieDetail(id: Int) {
        lifecycleScope.launchWhenResumed {
            progressBar.visibility = View.GONE
            val movie = withContext(Dispatchers.IO){
                try {
                    progressBar.visibility = View.GONE
                    val response = RetrofitMoviesService.getMovieApi()
                        .getMovieByIdCoroutine(id, BuildConfig.MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val post = response.body()
                        if (post != null) {
                            movieDao?.insert(post)
                        }
                        post
                    } else {
                        movieDao?.getMovie(id) ?: Movie()
                    }
                } catch (e: Exception) {
                    movieDao?.getMovie(id) ?: Movie()
                }
            }
            Glide.with(Image).load(movie?.getBackDropPathImage()).into(Image)
            MovieName.text = movie?.title
        }
    }
}