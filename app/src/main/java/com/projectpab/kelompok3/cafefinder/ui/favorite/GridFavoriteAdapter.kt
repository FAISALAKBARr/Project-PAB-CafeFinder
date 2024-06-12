package com.projectpab.kelompok3.cafefinder.ui.favorite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.CafeDetailActivity
import com.projectpab.kelompok3.cafefinder.R

class GridFavoriteAdapter(private val listFavorite: ArrayList<Cafe>, private val context: Context) : RecyclerView.Adapter<GridFavoriteAdapter.GridViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(cafe: Cafe)
    }

    private var onDeleteClickListener: OnDeleteClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteImage: ImageView = itemView.findViewById(R.id.favoriteImg)
        val cafeName: TextView = itemView.findViewById(R.id.cafeName)
        val ratingCafe: TextView = itemView.findViewById(R.id.ratingCafe)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val menuButton: ImageButton = itemView.findViewById(R.id.menuButton)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(listFavorite[position])
                }
            }

            menuButton.setOnClickListener {
                showBottomSheetMenu()
            }
        }

        private fun showBottomSheetMenu() {
            val bottomSheetDialog = BottomSheetDialog(context)
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_menu, null)
            bottomSheetDialog.setContentView(view)

            val closeButton = view.findViewById<ImageButton>(R.id.closeButton)
            val seeCafe = view.findViewById<TextView>(R.id.seeCafeBottomDialog)
            val seeLocation = view.findViewById<TextView>(R.id.seeLocationBottomDialog)
            val shareCafe = view.findViewById<TextView>(R.id.shareBottomDialog)
            val delete = view.findViewById<TextView>(R.id.deleteBottomDialog)

            val position = adapterPosition
            val cafe = listFavorite[position]

            closeButton.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            seeCafe.setOnClickListener {
                val intent = Intent(context, CafeDetailActivity::class.java).apply {
                    putExtra("SONG_NAME", cafe.name)
                    putExtra("SONG_DESC", cafe.description)
                    putExtra("SONG_IMG_RES_ID", cafe.imageId)
                }
                context.startActivity(intent)
                bottomSheetDialog.dismiss()
            }

            seeLocation.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    val url = context.resources.getStringArray(R.array.data_link_alamat)[position]
                    navigateToURL(url)
                }

                bottomSheetDialog.dismiss()
            }

            shareCafe.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    val name = cafe.name
                    val address = context.resources.getStringArray(R.array.data_alamat)[position]
                    val link = context.resources.getStringArray(R.array.data_link_alamat)[position]
                    shareCafeInfo(name, address, link)
                }
                bottomSheetDialog.dismiss()
            }

            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.onDeleteClick(listFavorite[position])
                }
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        }

        private fun shareCafeInfo(name: String, address: String, link: String) {
            val shareText = "Check out this café:\n\nName: $name\nAddress: $address\nGoogle Maps: $link"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        private fun navigateToURL(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_gridfavorite_cafe, parent, false)
        return GridViewHolder(view)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val (name, desc, img, category, recommendation, rating) = listFavorite[position]
        holder.cafeName.text = if (name.length > 9) name.substring(0, 9) + ".." else name
        holder.favoriteImage.setImageResource(img)
        holder.ratingCafe.text = rating.toString()
        holder.favoriteImage.setOnClickListener {
            val intent = Intent(holder.itemView.context, CafeDetailActivity::class.java).apply {
                putExtra("SONG_NAME", name)
                putExtra("SONG_DESC", desc)
                putExtra("SONG_IMG_RES_ID", img)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}