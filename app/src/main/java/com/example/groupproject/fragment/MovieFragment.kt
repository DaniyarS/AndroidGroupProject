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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment() {
    private lateinit var progressBar : ProgressBar
    private lateinit var Image : ImageView
    private lateinit var MovieName: TextView
    private lateinit var MovieGenre: TextView
    private lateinit var MovieIndex: TextView

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
        MovieIndex.text="1"
        getBriefMovieDetail(110)

        return view
    }

    private fun getBriefMovieDetail(id: Int) {
        RetrofitMoviesService.getMovieApi().getMovieById(id, BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
            Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                progressBar.visibility = View.GONE
                val post = response.body()
                if (post != null) {

                    Glide.with(Image).load(post.getBackDropPathImage()).into(Image)

                    MovieName.text = post.title

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
                }
            }
        })
    }

}