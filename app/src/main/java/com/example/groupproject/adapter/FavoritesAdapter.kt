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

class FavoritesAdapter(
    var listOfFavMovies: List<Movie>? = null,
    var context: Context,
    val itemClickListener: RecyclerViewItemClick? = null
): RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
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


            val movieTitle = view.findViewById<TextView>(R.id.tvMovieNameFav)
            val movieImage = view.findViewById<ImageView>(R.id.ivMovieFav)
            val removeFromFavList = view.findViewById<ImageView>(R.id.ivAddListFav)
            Glide.with(context).load(post?.getBackDropPathImage()).into(movieImage)
            movieTitle.text = post?.title


            view.setOnClickListener {
                itemClickListener?.itemClick(adapterPosition, post!!)
            }
            removeFromFavList.setOnClickListener {
                itemClickListener?.removeFromFavorites(adapterPosition,post!!)
                removeFromFavList.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
        }
    }

    interface RecyclerViewItemClick {
        fun itemClick(position: Int, item: Movie)
        fun removeFromFavorites(position: Int, item: Movie)
    }
}