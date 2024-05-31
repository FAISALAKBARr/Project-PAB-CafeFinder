package com.projectpab.kelompok3.cafefinder.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectpab.kelompok3.cafefinder.R
import android.content.Intent
import android.widget.TextView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout with the correct parent parameter
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.share)?.setOnClickListener {
            shareButton()
        }
    }

    private fun shareButton(){
        val nim = view?.findViewById<TextView>(R.id.nim)?.text.toString()
        val nama = view?.findViewById<TextView>(R.id.nama)?.text.toString()
        val jurusan = view?.findViewById<TextView>(R.id.jurusan)?.text.toString()
        val fakultas = view?.findViewById<TextView>(R.id.fakultas)?.text.toString()
        val universitas = view?.findViewById<TextView>(R.id.universitas)?.text.toString()
        val email = view?.findViewById<TextView>(R.id.email)?.text.toString()

        val  shareProfile = "NIM\t\t\t\t\t\t: $nim\nNama\t\t\t: $nama\nJurusan\t\t\t: $jurusan\nFakultas\t\t: $fakultas\n" +
                "Universitas\t: $universitas\nEmail\t\t\t\t\t\t: $email"

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareProfile)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent,"Share to"))
    }
}


