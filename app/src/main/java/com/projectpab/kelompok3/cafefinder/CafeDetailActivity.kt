package com.projectpab.kelompok3.cafefinder

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class CafeDetailActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var songAudio: Int = -1

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

        tvSongName.text = songName
        tvSongDesc.text = songDesc
        imgPhoto.setImageResource(songImage)

        mediaPlayer = MediaPlayer.create(this, songAudio)
    }

    fun gotoMain(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("SELECTED_TAB", R.id.navigation_dashboard)
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
