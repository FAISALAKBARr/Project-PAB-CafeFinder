package com.projectpab.kelompok3.cafefinder.ui.cafe

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.projectpab.kelompok3.cafefinder.R
import com.projectpab.kelompok3.cafefinder.databinding.FragmentCafeBinding
import com.projectpab.kelompok3.cafefinder.Cafe
import java.util.Locale

class CafeFragment : Fragment() {

    private var _binding: FragmentCafeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListCafeAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listCafe: ArrayList<Cafe>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", 0)
        listCafe = generateCafeList()

        sortCafesByRating()

        adapter = ListCafeAdapter(listCafe, requireContext())
        binding.rvCafe.adapter = adapter
        binding.rvCafe.layoutManager = LinearLayoutManager(context)

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
            sortCafesByRating()
            adapter.notifyDataSetChanged()
        }

        return root
    }




    private fun sortCafesByRating() {
        listCafe.forEach {
            it.rating = sharedPreferences.getFloat(it.name, 0f)
        }
        listCafe.sortByDescending { it.rating }
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
                adapter.setFilteredList(filteredList)
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
            adapter.setFilteredList(filteredList)
        }
    }

    private fun filterByRecommendation() {
        val filteredList = ArrayList<Cafe>()
        for (cafe in listCafe) {
            if (cafe.recommendation == "yes") {
                filteredList.add(cafe)
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
        } else {
            adapter.setFilteredList(filteredList)
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

    private fun generateCafeList(): ArrayList<Cafe> {
        val cafeNames = resources.getStringArray(R.array.data_cafe)
        val cafeDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val cafeImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val cafeAudios = resources.obtainTypedArray(R.array.data_audio)
        val categories = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)

        val cafeList = ArrayList<Cafe>()
        for (i in cafeNames.indices) {
            val cafe = Cafe(
                cafeNames[i],
                cafeDescriptions[i],
                cafeImages.getResourceId(i, -1),
                cafeAudios.getResourceId(i, -1),
                categories[i],
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                binding.rvCafe.layoutManager = LinearLayoutManager(context)
                true
            }
            R.id.action_grid -> {
                binding.rvCafe.layoutManager = GridLayoutManager(context, 2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
