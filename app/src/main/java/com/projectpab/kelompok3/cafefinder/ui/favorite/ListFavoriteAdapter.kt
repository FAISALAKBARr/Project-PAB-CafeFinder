package com.projectpab.kelompok3.cafefinder.ui.favorite

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.CafeDetailActivity
import com.projectpab.kelompok3.cafefinder.R

class ListFavoriteAdapter(private val listFavorite: ArrayList<Cafe>, private val context: Context) : RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)

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
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val gotoCafeButton: Button = itemView.findViewById(R.id.cafeButton)
        val locationCafeButton: Button = itemView.findViewById(R.id.locationButton)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(listFavorite[position])
                }
            }

            locationCafeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val url = context.resources.getStringArray(R.array.data_link_alamat)[position]
                    navigateToURL(url)
                }
            }
        }

        private fun navigateToURL(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_rowfavorite_cafe, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, desc, img) = listFavorite[position]
        holder.cafeName.text = if (name.length > 12) name.substring(0, 12) + "..." else name
        holder.favoriteImage.setImageResource(img)
        holder.gotoCafeButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, CafeDetailActivity::class.java).apply {
                putExtra("SONG_NAME", name)
                putExtra("SONG_DESC", desc)
                putExtra("SONG_IMG_RES_ID", img)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}
