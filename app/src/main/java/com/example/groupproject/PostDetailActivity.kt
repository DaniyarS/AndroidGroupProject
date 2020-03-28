package com.example.groupproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body

class PostDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        progressBar = findViewById(R.id.progressBar)
        tvTitle = findViewById(R.id.tvTitle)
        tvBody = findViewById(R.id.tvBody)

        val postId = intent.getIntExtra("movie_id", 1)
//        getPost(id = postId)

    }

//    private fun getPost(id: Int) {
//        RetrofitMoviesService.getMovieApi().getMovieById(id).enqueue(object : Callback<Movie> {
//            override fun onFailure(call: Call<Movie>, t: Throwable) {
//                progressBar.visibility = View.GONE
//            }
//
//            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
//                progressBar.visibility = View.GONE
//                val post = response.body()
//                if (post != null) {
//                    tvBody.text = post.title
//                    tvTitle.text = post.overview
//                }
//            }
//        })
//    }
}
