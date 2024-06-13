package com.projectpab.kelompok3.cafefinder.ui.home

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.projectpab.kelompok3.cafefinder.Cafe
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.projectpab.kelompok3.cafefinder.CafeDetailActivity
import com.projectpab.kelompok3.cafefinder.R

class RandomizedCafeAdapter(private val listFavorite: ArrayList<Cafe>) : RecyclerView.Adapter<RandomizedCafeAdapter.ListViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(cafe: Cafe)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteImage: ImageView = itemView.findViewById(R.id.favoriteImg)
        val cafeName: TextView = itemView.findViewById(R.id.cafeName)
        val ratingCafe: TextView = itemView.findViewById(R.id.ratingCafe)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_randomizedcafe_home, parent, false)
        return ListViewHolder(view)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, desc, img, category, recommendation, rating) = listFavorite[position]
        holder.cafeName.text = if (name.length > 12) name.substring(0, 12) + "..." else name
        holder.favoriteImage.setImageResource(img)
        holder.ratingCafe.text = rating.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CafeDetailActivity::class.java).apply {
                putExtra("SONG_NAME", name)
                putExtra("SONG_DESC", desc)
                putExtra("SONG_IMG_RES_ID", img)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}