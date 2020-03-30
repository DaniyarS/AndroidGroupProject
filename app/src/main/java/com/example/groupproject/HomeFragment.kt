package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.groupproject.adapter.MoviesAdapter
//import com.example.groupproject.adapter.MoviesPagerAdapter
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), MoviesAdapter.RecyclerViewItemClick {

    lateinit var recyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter?=null
    lateinit var listMovies: List<Movie>
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var topRatedRecyclerView: RecyclerView
    private var movies2Adapter: MoviesAdapter?=null
    lateinit var listTopRatedMovies: List<Movie>

    lateinit var upcomingRecyclerView: RecyclerView
    private var movies3Adapter: MoviesAdapter?=null
    lateinit var listUpcomingMovies: List<Movie>
//    private lateinit var viewPager: ViewPager
//    private lateinit var pagerAdapter: MoviesPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewMovies = inflater.inflate(R.layout.fragment_home,container,false)

//        viewPager = viewMovies.findViewById(R.id.vpHeadline)
//        pagerAdapter = MoviesPagerAdapter(supportFragmentManager, movies)
//        viewPager.adapter = pagerAdapter

        recyclerView = viewMovies.findViewById(R.id.mainRecyclerView)
        topRatedRecyclerView = viewMovies.findViewById(R.id.secondRecyclerView)
        upcomingRecyclerView = viewMovies.findViewById(R.id.thirdRecyclerView)

        swipeRefreshLayout = viewMovies.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
//            moviesAdapter?.clearAll()
            generateComponent()
        }


        generateComponent()

        return viewMovies

    }

    fun generateComponent(){

        listMovies = ArrayList<Movie>()

        moviesAdapter =activity?.applicationContext?.let {MoviesAdapter(listMovies, it,itemClickListener = this)  }
        recyclerView.layoutManager =   LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = moviesAdapter
        moviesAdapter?.notifyDataSetChanged()

        listTopRatedMovies = ArrayList<Movie>()
        movies2Adapter = activity?.applicationContext?.let{MoviesAdapter(listTopRatedMovies,it,itemClickListener = this)}
        topRatedRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        topRatedRecyclerView.adapter = movies2Adapter
        movies2Adapter?.notifyDataSetChanged()

        listUpcomingMovies = ArrayList<Movie>()
        movies3Adapter = activity?.applicationContext?.let{MoviesAdapter(listUpcomingMovies,it,itemClickListener = this)}
        upcomingRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        upcomingRecyclerView.adapter = movies3Adapter
        movies3Adapter?.notifyDataSetChanged()

        initPopularMovies()
        initTopRatedMovies()
        initUpcomingMovies()
    }

//
    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }


    fun initPopularMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getPopularMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
                Callback<GetMoviesResponse> {
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
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }

    fun initTopRatedMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getTopRatedMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
                Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("top_rated_movie_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        movies2Adapter?.ListOfMovies = list
                        movies2Adapter?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }

    fun initUpcomingMovies() {
        try {
            if (BuildConfig.MOVIE_DB_API_TOKEN.isEmpty()) {
                return;
            }
            swipeRefreshLayout.isRefreshing = true
            RetrofitMoviesService.getMovieApi().getUpcomingMovies(BuildConfig.MOVIE_DB_API_TOKEN).enqueue(object :
                Callback<GetMoviesResponse> {
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    swipeRefreshLayout.isRefreshing = false
                }
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>
                ) {
                    Log.d("upcoming_movie_list", response.body().toString())
                    if (response.isSuccessful) {
                        val list = response.body()?.results
                        movies3Adapter?.ListOfMovies = list
                        movies3Adapter?.notifyDataSetChanged()
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }) } catch (e: Exception) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT)
        }
    }


}
