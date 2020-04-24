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
import com.bumptech.glide.Glide
import com.example.groupproject.BuildConfig
import com.example.groupproject.R
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.database.MovieDao
import com.example.groupproject.database.MovieDatabase
import com.example.groupproject.model.Movie
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieFragmentFourth : Fragment(), CoroutineScope {

    private lateinit var progressBar: ProgressBar
    private lateinit var Image : ImageView
    private lateinit var MovieName: TextView
    private lateinit var MovieGenre: TextView
    private lateinit var MovieIndex: TextView

    //new val job
    private val job = Job()

    //override fun for coroutine context
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var movieDao : MovieDao?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.headline_movie_items, container,false)

        movieDao = MovieDatabase.getDatabase(context = activity!!).movieDao()

        progressBar = view.findViewById(R.id.progressBar)
        Image = view.findViewById(R.id.ivHeadlineMovie)
        MovieName = view.findViewById(R.id.tvMovieName)
        MovieGenre = view.findViewById(R.id.tvGenre)
        MovieIndex = view.findViewById(R.id.tvImageIndex)
        MovieIndex.text="4"
//        getBriefMovieDetail(440)
        getBriefMovieDetailCoroutine(155)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    @SuppressLint("SetTextI18n")
    private fun getBriefMovieDetailCoroutine(id: Int){
        launch {
            val movie = withContext(Dispatchers.IO){
                try{
                    progressBar.visibility = View.GONE
                    val response = RetrofitMoviesService.getMovieApi().
                    getMovieByIdCoroutine(id,BuildConfig.MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val post = response.body()
                        if (post!=null){
                            movieDao?.getBriefMovie(id)
                        }
                        post
                    } else {
                        movieDao?.getBriefMovie(id)
                    }
                } catch (e: Exception) {
                    movieDao?.getBriefMovie(id)
                }
            }
            progressBar.visibility = View.GONE
            Glide.with(Image).load("https://image.tmdb.org/t/p/original"+movie?.backdrop_path).into(Image)
            MovieName.text = movie?.title
            MovieGenre.text = ""
            //            var genreCounter = 1
//            for (genre in genreNameContainer!!) {
//                if (genreCounter == genreNameContainer.size) {
//                    MovieGenre.text = MovieGenre.text.toString() + genre.name
//                } else {
//                    MovieGenre.text = MovieGenre.text.toString() + genre.name + " • "
//                }
//                genreCounter += 1
            }
        }
    }
