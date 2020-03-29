package com.example.groupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), MoviesAdapter.RecyclerViewItemClick{

    lateinit var recyclerView: RecyclerView //recycler view init
    lateinit var swipeRefreshLayout: SwipeRefreshLayout //swiperefreshlayout init

    lateinit var recyclerView2: RecyclerView
    lateinit var recyclerView3: RecyclerView

    lateinit var listMovies: List<Movie>
    lateinit var listMovies2: List<Movie>
    lateinit var listMovies3: List<Movie>

    private var moviesAdapter: MoviesAdapter? = null
    private var moviesAdapter2: MoviesAdapter? = null
    private var moviesAdapter3: MoviesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout) //initlayout



        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            moviesAdapter?.clearAll()
            moviesAdapter2?.clearAll()
            moviesAdapter3?.clearAll()

            initPopularMovies()
            initTopRatedMovies()
            initUpcomingMovies()
        }

        generateComponent()



    }

        fun generateComponent(){

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView3 = findViewById(R.id.recyclerView3)

        listMovies = ArrayList<Movie>()
        listMovies2 = ArrayList<Movie>()
        listMovies3 = ArrayList<Movie>()


        moviesAdapter = MoviesAdapter(listMovies,this,itemClickListener = this)
        moviesAdapter2 = MoviesAdapter(listMovies2,this,itemClickListener = this)
        moviesAdapter3 = MoviesAdapter(listMovies3,this,itemClickListener = this)

        recyclerView.layoutManager =  LinearLayoutManager(this)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView3.layoutManager = LinearLayoutManager(this)
//        recyclerView.itemAnimator= DefaultItemAnimator()
        recyclerView.adapter = moviesAdapter
        recyclerView2.adapter=moviesAdapter2
        recyclerView3.adapter=moviesAdapter3

        moviesAdapter?.notifyDataSetChanged()
        moviesAdapter2?.notifyDataSetChanged()
        moviesAdapter3?.notifyDataSetChanged()


        initPopularMovies()
        initTopRatedMovies()
        initUpcomingMovies()
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
                        val list2 = response.body()?.results
                        moviesAdapter2?.ListOfMovies = list2
                        moviesAdapter2?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
        }
    }

    fun initUpcomingMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getUpcomingMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object : Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("upcoming_movie_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list3 = response.body()?.results
                        moviesAdapter3?.ListOfMovies = list3
                        moviesAdapter3?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT)
        }
    }




}
