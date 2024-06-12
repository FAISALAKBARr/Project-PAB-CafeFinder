package com.projectpab.kelompok3.cafefinder.ui.favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectpab.kelompok3.cafefinder.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.FavoriteManager
import com.projectpab.kelompok3.cafefinder.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListFavoriteAdapter
    private lateinit var listCafe: ArrayList<Cafe>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateFavoriteCount()
    }

    private fun setupRecyclerView() {
        listCafe = generateFavoriteCafeList()
        adapter = ListFavoriteAdapter(listCafe, requireContext())
        adapter.setOnDeleteClickListener(object : ListFavoriteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(cafe: Cafe) {
                showDeleteConfirmationDialog(cafe)
            }
        })

        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
    }


    private fun updateFavoriteCount() {
        val favoriteCount = FavoriteManager.getFavoriteCount(requireContext())
        val dispFavCount = binding.cafeFavoriteCount
        val oldcount = dispFavCount.text.toString()

        val newcount = oldcount.replace(Regex("^\\d+"), favoriteCount.toString())
        dispFavCount.text = newcount
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteFavoriteItem(deletedCafe: Cafe) {
        listCafe.remove(deletedCafe)
        adapter.notifyDataSetChanged()
        updateFavoriteCount()
        FavoriteManager.removeFavorite(requireContext(), deletedCafe)
    }

    private fun showDeleteConfirmationDialog(deletedCafe: Cafe) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin menghapus cafe ini dari favorit?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteFavoriteItem(deletedCafe)
            updateFavoriteCount()
        }
        builder.setNegativeButton("No") { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }


    private fun generateFavoriteCafeList(): ArrayList<Cafe> {
        val favoriteNames = FavoriteManager.getFavorites(requireContext())
        val allCafes = generateCafeList()
        val favoriteCafes = ArrayList<Cafe>()
        for (cafe in allCafes) {
            if (favoriteNames.contains(cafe.name)) {
                favoriteCafes.add(cafe)
            }
        }
        return favoriteCafes
    }

    private fun generateCafeList(): ArrayList<Cafe> {
        // Mengambil semua cafe dari resources (gunakan metode yang sama seperti di CafeFragment)
        val cafeNames = resources.getStringArray(R.array.data_cafe)
        val cafeDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val cafeImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val cafeAudios = resources.obtainTypedArray(R.array.data_audio)
        val category = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)

        val cafeList = ArrayList<Cafe>()
        for (i in cafeNames.indices) {
            val cafe = Cafe(
                cafeNames[i],
                cafeDescriptions[i],
                cafeImages.getResourceId(i, -1),
                cafeAudios.getResourceId(i, -1),
                category[i],
                recommended[i],
                0f, // Default rating, it will be updated later
            )
            cafeList.add(cafe)
        }
        cafeImages.recycle()
        cafeAudios.recycle()

        return cafeList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
