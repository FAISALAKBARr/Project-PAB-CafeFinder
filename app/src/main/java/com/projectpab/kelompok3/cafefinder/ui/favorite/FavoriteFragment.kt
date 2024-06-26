package com.projectpab.kelompok3.cafefinder.ui.favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.projectpab.kelompok3.cafefinder.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.FavoriteManager
import com.projectpab.kelompok3.cafefinder.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListFavoriteAdapter
    private lateinit var gridAdapter: GridFavoriteAdapter
    private lateinit var listCafe: ArrayList<Cafe>
    private var isGridLayout = false

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
        listCafe = generateFavoriteCafeList()
        setupRecyclerView()
        updateFavoriteCount()
        updateCafeRatings()

        binding.layoutButton.setOnClickListener {
            toggleLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        updateCafeRatings()
    }

    private fun setupRecyclerView() {
        listAdapter = ListFavoriteAdapter(listCafe, requireContext())
        listAdapter.setOnDeleteClickListener(object : ListFavoriteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(cafe: Cafe) {
                showDeleteConfirmationDialog(cafe)
            }
        })

        gridAdapter = GridFavoriteAdapter(listCafe, requireContext())
        gridAdapter.setOnDeleteClickListener(object : GridFavoriteAdapter.OnDeleteClickListener {
            override fun onDeleteClick(cafe: Cafe) {
                showDeleteConfirmationDialog(cafe)
            }
        })

        binding.rvFavorite.adapter = listAdapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
    }

    private fun toggleLayout() {
        isGridLayout = !isGridLayout
        if (isGridLayout) {
            binding.rvFavorite.adapter = gridAdapter
            binding.rvFavorite.layoutManager = GridLayoutManager(context, 2)
            binding.layoutButton.setImageResource(R.drawable.gridicon)
        } else {
            binding.rvFavorite.adapter = listAdapter
            binding.rvFavorite.layoutManager = LinearLayoutManager(context)
            binding.layoutButton.setImageResource(R.drawable.listicon)
        }
    }

    private fun updateFavoriteCount() {
        val favoriteCount = FavoriteManager.getFavoriteCount(requireContext())
        val dispFavCount = binding.cafeFavoriteCount
        val oldcount = dispFavCount.text.toString()
        val newcount = oldcount.replace(Regex("^\\d+"), favoriteCount.toString())
        dispFavCount.text = newcount
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCafeRatings() {
        val sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)
        for (cafe in listCafe) {
            val rating = sharedPreferences.getFloat(cafe.name, 0f)
            cafe.rating = rating
        }
        this.listAdapter.notifyDataSetChanged()
        this.gridAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteFavoriteItem(deletedCafe: Cafe) {
        listCafe.remove(deletedCafe)
        this.listAdapter.notifyDataSetChanged()
        this.gridAdapter.notifyDataSetChanged()
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
        val cafeNames = resources.getStringArray(R.array.data_cafe)
        val cafeDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val cafeImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val category = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)
        val sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)

        val cafeList = ArrayList<Cafe>()
        for (i in cafeNames.indices) {
            val rating = sharedPreferences.getFloat(cafeNames[i], 0f)
            val cafe = Cafe(
                cafeNames[i],
                cafeDescriptions[i],
                cafeImages.getResourceId(i, -1),
                category[i],
                recommended[i],
                rating,
            )
            cafeList.add(cafe)
        }
        cafeImages.recycle()

        return cafeList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}