package com.projectpab.kelompok3.cafefinder.ui.cafe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectpab.kelompok3.cafefinder.R
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.CafeDetailActivity

class GridCafeAdapter (private var listCafe: ArrayList<Cafe>,private val context: Context) : RecyclerView.Adapter<GridCafeAdapter.GridViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)

    private var isGridMode = true

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_song_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_song_description)
        val ratingCafe: TextView = itemView.findViewById(R.id.rating)
        val category: TextView = itemView.findViewById(R.id.category)
    }

    fun setFilteredList(listCafe: ArrayList<Cafe>){
        this.listCafe = listCafe
        notifyDataSetChanged()
    }

    fun sortListByRating() {
        listCafe.sortByDescending { sharedPreferences.getFloat(it.name, 0f) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val layoutRes = if (isGridMode) R.layout.item_grid_cafe else R.layout.item_row_cafe
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int = listCafe.size

    override fun onBindViewHolder(holder: GridCafeAdapter.GridViewHolder, position: Int) {
        val (name, desc, img, category, recommendation, rating) = listCafe[position]
        holder.imgPhoto.setImageResource(img)
        holder.tvName.text = name
        holder.tvDescription.text = desc
        holder.ratingCafe.text = rating.toString()
        holder.category.text = category
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CafeDetailActivity::class.java).apply {
                putExtra("SONG_NAME", name)
                putExtra("SONG_DESC", desc)
                putExtra("SONG_IMG_RES_ID", img)
                putExtra("CAFE_POSITION", position)
                putExtra("CAFE_MENU", position)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Cafe)
    }
}