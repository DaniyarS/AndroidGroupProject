package com.example.groupproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.groupproject.api.FavoriteRequest
import com.example.groupproject.api.FavoriteResponse
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailActivity : AppCompatActivity() {
    private val APP_PREFERENCES = "appsettings"
    private val APP_SESSION = "session_id"

    private lateinit var ivAddList:ImageView

    private lateinit var progressBar: ProgressBar

    private lateinit var movieImageBackdrop: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var movieRealease: TextView
    private lateinit var movieDuration: TextView
    private lateinit var movieGenre: TextView
    private lateinit var movieDetails: TextView
    private lateinit var movieDirector: TextView
    private lateinit var movieCast: TextView
    private lateinit var btnFavorite: ImageView
    private var counter: Int = 0
//    var list: MutableList<Int> = ArrayList()

    private lateinit var getSP : SharedPreferences
    private lateinit var session_id: String
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_items)

        ivAddList = findViewById(R.id.ivAddList)
        getSP = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)!!
        if (getSP.contains(APP_SESSION)){
            session_id = getSP.getString(APP_SESSION,"null")!!
        }

        progressBar = findViewById(R.id.progressBar)
        movieImageBackdrop = findViewById(R.id.ivMovie)
        movieTitle = findViewById(R.id.tvMovieName)
        movieRealease= findViewById(R.id.tvYear)
        movieDuration= findViewById(R.id.tvDuration)
        movieGenre= findViewById(R.id.textView6)
        movieDetails = findViewById(R.id.tvDetailDesc)
        movieDirector= findViewById(R.id.tvDirectorName)
        movieCast= findViewById(R.id.tvCastName)
        btnFavorite = findViewById(R.id.ivAddList)

        val movieId= intent.getIntExtra("movie_id", 1)
        getMovieDetail(id = movieId)
        getCredits(id = movieId)

//        Toast.makeText(this, movieId.toString(), Toast.LENGTH_SHORT).show()
////        ivAddList.setOnClickListener {
////            if (AccountFragment().isSigned){
////                val bundle = Bundle()
////                bundle.putInt("movie_id", movieId)
////                //list.add(movieId)
////                //SelectFragment().favlist.add(movieId)
////                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
////            }
////            else{
////                val intent = Intent(this, SelectFragment::class.java)
////                intent.putExtra("movie_id", movieId)
////                Toast.makeText(this, "Please, sign in first", Toast.LENGTH_SHORT).show()
////            }
////        }

        ivAddList.setOnClickListener(){
            addToFavoriteMovie(movieId)
        }
    }

    private fun addToFavoriteMovie(item: Int){

        lateinit var favoriteRequest: FavoriteRequest

        if (isClicked==false){
            isClicked=true
            favoriteRequest=FavoriteRequest("movie",item, isClicked)
            ivAddList.setImageResource(R.drawable.ic_star_black_24dp)
            RetrofitMoviesService.getMovieApi().addFavorite(BuildConfig.MOVIE_DB_API_TOKEN,session_id,favoriteRequest).enqueue(object: Callback<FavoriteResponse>{

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    Toast.makeText(this@MovieDetailActivity , "NOT ADDED", Toast.LENGTH_LONG).show() }

                override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                    Toast.makeText(this@MovieDetailActivity, "ADDED", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else{
            isClicked=false
            ivAddList.setImageResource(R.drawable.ic_star_border_black_24dp)
            favoriteRequest=FavoriteRequest("movie",item, isClicked)
            RetrofitMoviesService.getMovieApi().addFavorite(BuildConfig.MOVIE_DB_API_TOKEN, session_id, favoriteRequest).enqueue(object: Callback<FavoriteResponse>{

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {}

                override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                    Toast.makeText(this@MovieDetailActivity, "DELETED", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getMovieDetail(id: Int) {
        RetrofitMoviesService.getMovieApi().getMovieById(id,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                progressBar.visibility = View.GONE
                val post = response.body()
                if (post != null) {
                    Glide.with(movieImageBackdrop).load(post.getBackDropPathImage()).into(movieImageBackdrop)

                    movieTitle.text = post.title

                    val realeaseDate = post.release_date
                    movieRealease.text = "(" + realeaseDate.substring(0,4) + ")"

                    val runtime = post.runtime
                    if (runtime>60) {
                        val runtimeHours = runtime/60
                        val runtimeMinutes = runtime%60
                        movieDuration.text=runtimeHours.toString()+"h " + runtimeMinutes.toString() + "min"
                    }
                    else {
                    movieDuration.text= "$runtime min"
                    }

                    val genreNameContainer = post.genres
                    movieGenre.text=""
                    var genreCounter = 1
                    for (genre in genreNameContainer){
                        if (genreCounter == genreNameContainer.size) {
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()}
                        else{
                            movieGenre.text = movieGenre.text.toString() + genre.getGenreName()+ " • "}
                        genreCounter += 1
                    }
                    movieDetails.text=post.overview

               }
            }
        })
    }

    private fun getCredits(id: Int) {
        RetrofitMoviesService.getMovieApi().getCredits(id,BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object: Callback<Credits> {
            override fun onFailure(call: Call<Credits>, t: Throwable) {
                TODO("Not yet implemented")
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
                val creditsBody = response.body()
                if (creditsBody!=null){
                    val crewCointainer = creditsBody.crew
                    for (crew in crewCointainer){
                        if (crew.getDirectorName() == "Producer"){
                            movieDirector.text = crew.name
                        }
                    }
                    movieCast.text=""
                    var movieCastCounter = 0
                    val castContainer = creditsBody.cast
                    for (cast in castContainer){
                        if (movieCastCounter == 3){
                            break
                        }
                        movieCast.text= movieCast.text.toString() + cast.getCastName() + " "
                        movieCastCounter += 1
                    }
                }
            }
        })
    }
}