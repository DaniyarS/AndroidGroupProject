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
import com.example.groupproject.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MovieFragmentThird : Fragment(), CoroutineScope {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.headline_movie_items, container,false)

        progressBar = view.findViewById(R.id.progressBar)
        Image = view.findViewById(R.id.ivHeadlineMovie)
        MovieName = view.findViewById(R.id.tvMovieName)
        MovieGenre = view.findViewById(R.id.tvGenre)
        MovieIndex = view.findViewById(R.id.tvImageIndex)
        MovieIndex.text="3"
//        getBriefMovieDetail(330)
        getBriefMovieDetailCoroutine(330)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

//    private fun getBriefMovieDetail(id: Int) {
//        RetrofitMoviesService.getMovieApi().getMovieById(id, BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
//            Callback<Movie> {
//            override fun onFailure(call: Call<Movie>, t: Throwable) {
//                progressBar.visibility = View.GONE
//            }
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
//                progressBar.visibility = View.GONE
//                val post = response.body()
//                if (post != null) {
//
//                    Glide.with(Image).load(post.getBackDropPathImage()).into(Image)
//
//                    MovieName.text = post.title
//
//                    val genreNameContainer = post.genres
//                    MovieGenre.text=""
//                    var genreCounter = 1
//                    for (genre in genreNameContainer){
//                        if (genreCounter == genreNameContainer.size) {
//                            MovieGenre.text = MovieGenre.text.toString() + genre.getGenreName()}
//                        else{
//                            MovieGenre.text = MovieGenre.text.toString() + genre.getGenreName()+ " • "}
//                        genreCounter += 1
//                    }
//                }
//            }
//        })
//    }

    @SuppressLint("SetTextI18n")
    private fun getBriefMovieDetailCoroutine(id: Int){
        launch {
            val response: Response<Movie> = RetrofitMoviesService.getMovieApi().
            getMovieByIdCoroutine(id,BuildConfig.MOVIE_DB_API_TOKEN)
            if (response.isSuccessful){
                val post = response.body()
                if (post != null) {
                    progressBar.visibility = View.GONE
//                    Glide.with(Image).load(post.getBackDropPathImage()).into(Image)
                    MovieName.text = post.title
//                    val genreNameContainer = post.genres
//                    MovieGenre.text=""
//                    var genreCounter = 1
//                    for (genre in genreNameContainer){
//                        if (genreCounter == genreNameContainer.size) {
//                            MovieGenre.text = MovieGenre.text.toString() + genre.name}
//                        else{
//                            MovieGenre.text = MovieGenre.text.toString() + genre.name+ " • "}
//                        genreCounter += 1
                    }
                }
            }
        }
    }
//}