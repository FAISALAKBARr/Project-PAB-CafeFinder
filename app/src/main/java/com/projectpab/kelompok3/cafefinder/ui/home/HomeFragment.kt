package com.projectpab.kelompok3.cafefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projectpab.kelompok3.cafefinder.FavoriteManager
import com.projectpab.kelompok3.cafefinder.databinding.FragmentHomeBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //super.onCreate(savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US)
        if (dateFormat is SimpleDateFormat) {
            dateFormat.applyPattern("EEEE, dd MMMM yyyy")
        }
        val date = dateFormat.format(Date())
        val dispDate = binding.date
        dispDate.text = date
        updateFavoriteCount()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateFavoriteCount()
    }

    private fun updateFavoriteCount() {
        val favoriteCount = FavoriteManager.getFavoriteCount(requireContext())
        val dispFavCount = binding.favCount
        dispFavCount.text = favoriteCount.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}