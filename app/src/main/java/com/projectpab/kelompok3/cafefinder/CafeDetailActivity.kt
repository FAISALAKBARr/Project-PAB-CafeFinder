package com.projectpab.kelompok3.cafefinder

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CafeDetailActivity : AppCompatActivity() {
    private lateinit var cafe: Cafe
    private lateinit var btnFavorite: ImageButton
    private lateinit var ratingBarDisplay: RatingBar
    private lateinit var ratingBarInput: RatingBar
    private lateinit var btnSubmitRating: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var linkAlamat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_detail)

        sharedPreferences = getSharedPreferences("CafeFinderPrefs", MODE_PRIVATE)

        val songName = intent.getStringExtra("SONG_NAME")
        val songDesc = intent.getStringExtra("SONG_DESC")
        val songImage = intent.getIntExtra("SONG_IMG_RES_ID", R.id.img_item_photo)
        val cafePosition = intent.getIntExtra("CAFE_POSITION", -1)

        val tvSongName: TextView = findViewById(R.id.tv_song_name)
        val tvSongDesc: TextView = findViewById(R.id.tv_song_description)
        val imgPhoto: ImageView = findViewById(R.id.img_item_photo)
        btnFavorite = findViewById(R.id.btn_favorite)
        ratingBarDisplay = findViewById(R.id.ratingBarDisplay)
        ratingBarInput = findViewById(R.id.ratingBarInput)
        btnSubmitRating = findViewById(R.id.btnSubmitRating)

        tvSongName.text = songName
        tvSongDesc.text = songDesc
        imgPhoto.setImageResource(songImage)

        val initialRating = sharedPreferences.getFloat(songName, 0f)
        cafe = Cafe(songName!!, songDesc!!, songImage, "", "", initialRating)

        updateFavoriteIcon()

        if (cafePosition != -1) {
            val dataLinkAlamat = resources.getStringArray(R.array.data_link_alamat)
            linkAlamat = dataLinkAlamat[cafePosition]
        }

        val btnLocation: ImageButton = findViewById(R.id.btn_location)
        btnLocation.setOnClickListener {
            openLocation()
        }

        btnFavorite.setOnClickListener {
            toggleFavorite()
        }
        btnSubmitRating.setOnClickListener {
            submitRating()
        }
        Log.d("CafeDetailActivity", "songName: $songName, songDesc: $songDesc, songImage: $songImage")

        // Display the current rating
        displayCurrentRating()
    }

    fun openLocation() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(linkAlamat)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun displayCurrentRating() {
        // Here, you would fetch the current rating from your data source, for simplicity, let's assume it's 4.0
        val currentRating = sharedPreferences.getFloat(cafe.name, 0f)
        ratingBarDisplay.rating = currentRating
    }

    private fun submitRating() {
        val newRating = ratingBarInput.rating
        val editor = sharedPreferences.edit()
        editor.putFloat(cafe.name, newRating)
        editor.apply()
        // Here, you would save the new rating to your data source
        // For demonstration, we just log it
        Log.d("CafeDetailActivity", "New rating submitted: $newRating")
        // Optionally, update the display rating after submission
        displayCurrentRating()
    }

    private fun toggleFavorite() {
        if (FavoriteManager.isFavorite(this, cafe)) {
            FavoriteManager.removeFavorite(this, cafe)
        } else {
            FavoriteManager.addFavorite(this, cafe)
        }
        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        if (FavoriteManager.isFavorite(this, cafe)) {
            btnFavorite.setImageResource(R.drawable.baseline_favorite_red_24) // icon untuk sudah favorite
        } else {
            btnFavorite.setImageResource(R.drawable.favorite_24dp_fill_white) // icon untuk belum favorite
        }
    }

    fun gotoMain(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("SELECTED_TAB", R.id.navigation_cafe)
        }
        startActivity(intent)
    }
}