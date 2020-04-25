package com.example.groupproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieFragment : Fragment(), CoroutineScope {
    private lateinit var progressBar : ProgressBar
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
        MovieIndex.text="1"
//        getBriefMovieDetail(110)
        getBriefMovieDetailCoroutine(13)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    @SuppressLint("SetTextI18n")
    private fun getBriefMovieDetailCoroutine(id: Int){
        launch {
            progressBar.visibility = View.GONE
            val movie = withContext(Dispatchers.IO){
//                try {
//                    progressBar.visibility = View.GONE
//                    val response = RetrofitMoviesService.getMovieApi()
//                        .getMovieByIdCoroutine(id, BuildConfig.MOVIE_DB_API_TOKEN)
//                    if (response.isSuccessful) {
//                        val post = response.body()
//                        if (post!=null){
//                            movieDao?.getBriefMovie(id)
//                        }
//                        post
//                    } else {
//                        movieDao?.getBriefMovie(id)
//                    }
//                } catch (e: Exception) {
//                    movieDao?.getBriefMovie(id)
//                }
                movieDao?.getBriefMovie(id) ?: Movie()
            }
//            progressBar.visibility = View.GONE
            Glide.with(Image).load("https://image.tmdb.org/t/p/original"+movie.backdrop_path).into(Image)
            MovieName.text = movie.title
            MovieGenre.text = ""
//            val genreNameContainer = movie?.genres
//            var genreCounter = 1
//            for (genre in genreNameContainer!!) {
//                if (genreCounter == genreNameContainer.size) {
//                    MovieGenre.text = MovieGenre.text.toString() + genre.name
//                } else {
//                    MovieGenre.text = MovieGenre.text.toString() + genre.name + " • "
//                }
//                genreCounter += 1
//            }

//            val response: Response<Movie> = RetrofitMoviesService.getMovieApi().
//            getMovieByIdCoroutine(id,BuildConfig.MOVIE_DB_API_TOKEN)
//            if (response.isSuccessful){
//                val post = response.body()
//                if (post != null) {
//                    progressBar.visibility = View.GONE
//                    Glide.with(Image).load("https://image.tmdb.org/t/p/original"+post.backdrop_path).into(Image)
//                    MovieName.text = post.title
//                    val genreNameContainer = post.genres
//                    MovieGenre.text=""
//                    var genreCounter = 1
////                    for (genre in genreNameContainer!!){
////                        if (genreCounter == genreNameContainer.size) {
////                            MovieGenre.text = MovieGenre.text.toString() + genre.name}
////                        else{
////                            MovieGenre.text = MovieGenre.text.toString() + genre.name+ " • "}
////                        genreCounter += 1
        }
    }
}
