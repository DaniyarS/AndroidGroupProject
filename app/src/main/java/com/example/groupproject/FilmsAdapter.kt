package com.example.groupproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmsAdapter (
    var list: List<Movie>? = null,
    var itemClickListener: RecyclerViewItemClick? = null
    ): RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FilmViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_film, p0, false)
        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(p0: FilmViewHolder, p1: Int) {
        p0.bind(list?.get(p1))
    }

    fun clearAll() {
        (list as? ArrayList<Movie>)?.clear()
        notifyDataSetChanged()
    }

    inner class FilmViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(post: Movie?) {
//            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
//            val tvPostId = view.findViewById<TextView>(R.id.tvPostId)
//            val tvUserId = view.findViewById<TextView>(R.id.tvUserId)
//
//            tvTitle.text = post?.title
//            tvPostId.text = post?.id.toString()
//            tvUserId.text = post?.overview

            view.setOnClickListener {
                itemClickListener?.itemClick(adapterPosition, post!!)
            }
        }
    }

    interface RecyclerViewItemClick{

        fun itemClick(position: Int, item: Movie)
    }

}
