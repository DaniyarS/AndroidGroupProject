package com.example.groupproject



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.groupproject.adapter.MoviesAdapter
import com.example.groupproject.adapter.ViewPagerAdapter
import com.example.groupproject.api.RetrofitMoviesService
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Movie

import retrofit2.Response
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment(), MoviesAdapter.RecyclerViewItemClick, CoroutineScope {

    private lateinit var recyclerView: RecyclerView
    private var moviesAdapter: MoviesAdapter?=null
    private lateinit var listMovies: List<Movie>
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var topRatedRecyclerView: RecyclerView
    private var movies2Adapter: MoviesAdapter?=null
    private lateinit var listTopRatedMovies: List<Movie>

    private lateinit var upcomingRecyclerView: RecyclerView
    private var movies3Adapter: MoviesAdapter?=null
    private lateinit var listUpcomingMovies: List<Movie>

    private lateinit var viewPager: ViewPager
    private lateinit var  pagerAdapter: ViewPagerAdapter

    //new val job
    private val job = Job()

    //override fun for coroutine context
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewMovies = inflater.inflate(R.layout.fragment_home,container,false)

        //initializing values
        recyclerView = viewMovies.findViewById(R.id.mainRecyclerView)
        topRatedRecyclerView = viewMovies.findViewById(R.id.secondRecyclerView)
        upcomingRecyclerView = viewMovies.findViewById(R.id.thirdRecyclerView)

        viewPager = viewMovies.findViewById(R.id.vpHeadline)
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter

        swipeRefreshLayout = viewMovies.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            moviesAdapter?.clearAll()
            movies2Adapter?.clearAll()
            movies3Adapter?.clearAll()
            generateComponent()
        }

        generateComponent()

        return viewMovies
    }

    private fun generateComponent(){

        listMovies = ArrayList()
        moviesAdapter =activity?.applicationContext?.let {MoviesAdapter(listMovies, it,itemClickListener = this)  }
        recyclerView.layoutManager =   LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = moviesAdapter

        listTopRatedMovies = ArrayList()
        movies2Adapter = activity?.applicationContext?.let{MoviesAdapter(listTopRatedMovies,it,itemClickListener = this)}
        topRatedRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        topRatedRecyclerView.adapter = movies2Adapter

        listUpcomingMovies = ArrayList()
        movies3Adapter = activity?.applicationContext?.let{MoviesAdapter(listUpcomingMovies,it,itemClickListener = this)}
        upcomingRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        upcomingRecyclerView.adapter = movies3Adapter

        initPopularMoviesCoroutine()  //coroutinePopularMovies
        initTopRatedMoviesCoroutine() //coroutineTopRated
        initUpcomingMoviesCoroutine() //coroutineUpcoming
    }

    override fun itemClick(position: Int, item: Movie) {
        val intent = Intent(activity, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", item.id)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    //Binding data to the first recycler view
    private fun initPopularMoviesCoroutine(){
            launch {
                swipeRefreshLayout.isRefreshing = true
                val response: Response<GetMoviesResponse> = RetrofitMoviesService.getMovieApi().
                getPopularMoviesCoroutine(BuildConfig.MOVIE_DB_API_TOKEN)
                if (response.isSuccessful){
                    val list = response.body()?.results
                    moviesAdapter?.ListOfMovies = list
                    moviesAdapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            }
    }

    //Binding data to the second recycler view
    private fun initTopRatedMoviesCoroutine() {
            launch {
                swipeRefreshLayout.isRefreshing = true
                val response: Response<GetMoviesResponse> = RetrofitMoviesService.getMovieApi()
                    .getTopRatedMoviesCoroutine(BuildConfig.MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    movies2Adapter?.ListOfMovies = list
                    movies2Adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            }
    }

    //Binding data to the second recycler view
    private fun initUpcomingMoviesCoroutine() {
            launch {
                swipeRefreshLayout.isRefreshing = true
                val response: Response<GetMoviesResponse> = RetrofitMoviesService.getMovieApi()
                    .getUpcomingMoviesCoroutine(BuildConfig.MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    movies3Adapter?.ListOfMovies = list
                    movies3Adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }
                swipeRefreshLayout.isRefreshing = false
            }
    }
}
