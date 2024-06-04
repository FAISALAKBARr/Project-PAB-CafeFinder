package com.projectpab.kelompok3.cafefinder

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class CafeDetailActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var songAudio: Int = -1
    private lateinit var cafe: Cafe
    private lateinit var btnFavorite: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe_detail)

        val songName = intent.getStringExtra("SONG_NAME")
        val songDesc = intent.getStringExtra("SONG_DESC")
        val songImage = intent.getIntExtra("SONG_IMG_RES_ID", R.id.img_item_photo)
        songAudio = intent.getIntExtra("SONG_AUDIO_RES_ID", -1)

        val tvSongName: TextView = findViewById(R.id.tv_song_name)
        val tvSongDesc: TextView = findViewById(R.id.tv_song_description)
        val imgPhoto: CircleImageView = findViewById(R.id.img_item_photo)
        btnFavorite = findViewById(R.id.btn_favorite)

        tvSongName.text = songName
        tvSongDesc.text = songDesc
        imgPhoto.setImageResource(songImage)

        if (songAudio != -1) {
            mediaPlayer = MediaPlayer.create(this, songAudio)
        }

        cafe = Cafe(songName!!, songDesc!!, songImage, songAudio, "", "")

        updateFavoriteIcon()

        btnFavorite.setOnClickListener {
            toggleFavorite()
        }
        Log.d("CafeDetailActivity", "songName: $songName, songDesc: $songDesc, songImage: $songImage, songAudio: $songAudio")
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
            btnFavorite.setImageResource(R.drawable.favorite_24dp_fill) // icon untuk belum favorite
        }
    }

    fun gotoMain(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("SELECTED_TAB", R.id.navigation_cafe)
        }
        startActivity(intent)
    }

    fun playSong(view: View) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songAudio)
        }
        mediaPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
