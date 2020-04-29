package com.example.groupproject.fragment

import android.os.Bundle
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
import kotlin.coroutines.CoroutineContext

class MovieFragmentEigth : Fragment(), CoroutineScope {

    private lateinit var progressBar: ProgressBar
    private lateinit var image: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieGenre: TextView
    private lateinit var movieIndex: TextView

    //new val job
    private val job = Job()

    private var movieDao: MovieDao? = null

    //override fun for coroutine context
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.headline_movie_items, container, false)

        movieDao = MovieDatabase.getDatabase(context!!).movieDao()
        val id = 880
        progressBar = view.findViewById(R.id.progressBar)
        image = view.findViewById(R.id.ivHeadlineMovie)
        movieName = view.findViewById(R.id.tvMovieName)
        movieGenre = view.findViewById(R.id.tvGenre)
        movieIndex = view.findViewById(R.id.tvImageIndex)
        movieIndex.text = "8"
        getBriefMovieDetail(id)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun getBriefMovieDetail(id: Int) {
        lifecycleScope.launchWhenResumed {
            progressBar.visibility = View.GONE
            val movie = withContext(Dispatchers.IO) {
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
            Glide.with(image).load(movie?.getBackDropPathImage()).into(image)
            movieName.text = movie?.title
        }
    }
}