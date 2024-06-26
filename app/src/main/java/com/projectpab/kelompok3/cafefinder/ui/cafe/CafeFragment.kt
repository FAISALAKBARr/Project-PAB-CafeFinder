package com.projectpab.kelompok3.cafefinder.ui.cafe

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.projectpab.kelompok3.cafefinder.R
import com.projectpab.kelompok3.cafefinder.databinding.FragmentCafeBinding
import com.projectpab.kelompok3.cafefinder.Cafe
import java.util.Locale

class CafeFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var _binding: FragmentCafeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListCafeAdapter
    private lateinit var gridAdapter: GridCafeAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listCafe: ArrayList<Cafe>
    private var sortByRatingOnRecommendation: Boolean = false
    private var isGridLayoutManager: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentCafeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", 0)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        listCafe = generateCafeList()

        sortCafesByRating()

        listAdapter = ListCafeAdapter(listCafe, requireContext())
        gridAdapter = GridCafeAdapter(listCafe, requireContext())


        binding.rvCafe.layoutManager = LinearLayoutManager(context)
        binding.rvCafe.adapter = listAdapter
        isGridLayoutManager = false

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        binding.btnCategory.setOnClickListener {
            showCategoryDialog()
        }

        binding.btnRecommendation.setOnClickListener {
            sortByRatingOnRecommendation = true
            sortCafesByRating()
            updateAdapter()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        updateCafeRatings()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (sortByRatingOnRecommendation) {
            sortCafesByRating()
          updateAdapter()
        }
    }

    private fun updateAdapter() {
        if (isGridLayoutManager) {
            gridAdapter.sortListByRating()
        } else {
            listAdapter.sortListByRating()
        }
    }

    private fun sortCafesByRating() {
        if (sortByRatingOnRecommendation) {
            listCafe.forEach {
                it.rating = sharedPreferences.getFloat(it.name, 0f)
            }
            listCafe.sortByDescending { it.rating }
        }
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Cafe>()
            for (cafe in listCafe) {
                if (cafe.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    filteredList.add(cafe)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
            } else {
                if (isGridLayoutManager) {
                    gridAdapter.setFilteredList(filteredList)
                } else {
                    listAdapter.setFilteredList(filteredList)
                }
            }
        }
    }

    private fun filterByCategory(selectedCategories: List<String>) {
        val filteredList = ArrayList<Cafe>()
        for (cafe in listCafe) {
            if (selectedCategories.contains(cafe.category)) {
                filteredList.add(cafe)
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
        } else {
            listAdapter.setFilteredList(filteredList)
            gridAdapter.setFilteredList(filteredList)
        }
    }

    private fun showCategoryDialog() {
        val categories = resources.getStringArray(R.array.data_categories)
        val selectedCategories = booleanArrayOf(*BooleanArray(categories.size))

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Categories")
        builder.setMultiChoiceItems(categories, selectedCategories) { _, which, isChecked ->
            selectedCategories[which] = isChecked
        }

        builder.setPositiveButton("OK") { _, _ ->
            val selectedCategoryList = mutableListOf<String>()
            for (i in selectedCategories.indices) {
                if (selectedCategories[i]) {
                    selectedCategoryList.add(categories[i])
                }
            }
            filterByCategory(selectedCategoryList)
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onResume() {
        super.onResume()
        updateCafeRatings()
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

    private fun generateCafeList(): ArrayList<Cafe> {
        val cafeNames = resources.getStringArray(R.array.data_cafe)
        val cafeDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val cafeImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val categories = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)
        val sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)

        val cafeList = ArrayList<Cafe>()
        for (i in cafeNames.indices) {
            val rating = sharedPreferences.getFloat(cafeNames[i], 0f)
            val cafe = Cafe(
                cafeNames[i],
                cafeDescriptions[i],
                cafeImages.getResourceId(i, -1),
                categories[i],
                recommended[i],
                rating //
            )
            cafeList.add(cafe)
        }
        cafeImages.recycle()

        return cafeList
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                if (isGridLayoutManager) {
                    binding.rvCafe.layoutManager = LinearLayoutManager(context)
                    binding.rvCafe.adapter = listAdapter
                    isGridLayoutManager = false
                    true
                } else {
                    false
                }
            }
            R.id.action_grid -> {
                if (!isGridLayoutManager) {
                    binding.rvCafe.layoutManager = GridLayoutManager(context, 2)
                    binding.rvCafe.adapter = gridAdapter
                    isGridLayoutManager = true
                    true
                } else {
                    false
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        _binding = null
    }

}
