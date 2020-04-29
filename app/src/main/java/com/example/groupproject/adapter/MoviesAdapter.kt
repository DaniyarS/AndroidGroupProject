package com.example.groupproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groupproject.R
import com.example.groupproject.model.Movie

class MoviesAdapter(
    var listMovies: List<Movie>? = null,
    var context: Context,
    val itemClickListener: RecyclerViewItemClick? = null
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_items, p0, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = listMovies?.size ?: 0

    override fun onBindViewHolder(p0: MovieViewHolder, p1: Int) {
        p0.bind(listMovies?.get(p1))
    }

    fun clearAll() {
        (listMovies as? ArrayList<Movie>)?.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(post: Movie?) {

            val movieTitle = view.findViewById<TextView>(R.id.tvMovieName)
            val movieImage = view.findViewById<ImageView>(R.id.ivMovie)

            Glide.with(context).load(post?.getPosterPathImage()).into(movieImage)
            movieTitle.text = post?.title

            view.setOnClickListener {
                itemClickListener?.itemClick(adapterPosition, post!!)
            }
        }
    }

    interface RecyclerViewItemClick {
        fun itemClick(position: Int, item: Movie)
    }
}