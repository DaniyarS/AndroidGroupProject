package com.example.groupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(),MoviesAdapter.RecyclerViewItemClick{

    lateinit var recyclerView: RecyclerView //recycler view init
    lateinit var swipeRefreshLayout: SwipeRefreshLayout //swiperefreshlayout init

    lateinit var listMovies: List<Movie>

    private var moviesAdapter: MoviesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout) //initlayout

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            moviesAdapter?.clearAll()
            initPopularMovies()
        }


        generateComponent()


    }

        fun generateComponent(){
        recyclerView = findViewById(R.id.recyclerView)

        listMovies = ArrayList<Movie>()
        moviesAdapter = MoviesAdapter(listMovies,this,itemClickListener = this)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.itemAnimator= DefaultItemAnimator()
        recyclerView.adapter = moviesAdapter
        moviesAdapter?.notifyDataSetChanged()

        initPopularMovies()
            initTopRatedMovies()

    }

    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }


    fun initPopularMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getPopularMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object : Callback<GetMoviesResponse> {
                    override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                    ) {
                        Log.d("popular_movie_list", response.body().toString())
                        if (response.isSuccessful) {
                            val list = response.body()?.results
                            moviesAdapter?.ListOfMovies = list
                            moviesAdapter?.notifyDataSetChanged()
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }
                }) } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
        }
    }

    fun initTopRatedMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getTopRatedMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object : Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("top_rated_movie_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        moviesAdapter?.ListOfMovies = list
                        moviesAdapter?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
        }
    }




}
