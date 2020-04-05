package com.example.groupproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groupproject.R
import com.example.groupproject.model.Movie
import kotlinx.android.synthetic.main.movie_detail_items.view.*

class FavoritesAdapter(
    var listOfFavMovies: List<Movie>? = null,
    var context: Context): RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_movie_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = listOfFavMovies?.size ?: 0

    override fun onBindViewHolder(p0: FavoritesViewHolder, p1: Int) {
        p0.bind(listOfFavMovies?.get(p1))
    }

    fun clearAll(){
        (listOfFavMovies as? ArrayList<Movie>)?.clear()
        notifyDataSetChanged()
    }

    inner class FavoritesViewHolder(val view: View): RecyclerView.ViewHolder(view)
    {
        fun bind(post: Movie?){
            val movieTitle = view.findViewById<TextView>(R.id.tvMovieName)
            val movieImage = view.findViewById<ImageView>(R.id.ivMovie)

            Glide.with(context).load(post?.getPosterPathImage()).into(movieImage)
            movieTitle.text = post?.title
        }
    }
}