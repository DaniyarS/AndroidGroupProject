package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MoviesAdapter(
    var ListOfMovies: List<Movie>? = null,
    var context: Context,
    val itemClickListener: RecyclerViewItemClick? = null
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_film, p0, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = ListOfMovies?.size ?: 0

    override fun onBindViewHolder(p0: MovieViewHolder, p1: Int) {
        p0.bind(ListOfMovies?.get(p1))
    }

    fun clearAll(){
        (ListOfMovies as? ArrayList<Movie>)?.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(post: Movie?) {
            val movieTitle = view.findViewById<TextView>(R.id.Movie_Title)
            val movieImage= view.findViewById<ImageView>(R.id.Movie_Image)

            movieTitle.text = post?.title
            Glide.with(context).load(post?.getPosterPathImage()).into(movieImage)

            view.setOnClickListener {
                itemClickListener?.itemClick(adapterPosition, post!!)
            }


        }
    }

    interface RecyclerViewItemClick {

        fun itemClick(position: Int, item: Movie)
    }

}