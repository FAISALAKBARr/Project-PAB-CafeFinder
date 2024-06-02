package com.projectpab.kelompok3.cafefinder.ui.cafe

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private lateinit var listCafe: ArrayList<Cafe>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCafeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listCafe = generateSongList()

        adapter = ListCafeAdapter(generateSongList())
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
            filterByRecommendation()
        }

        return root
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Cafe>()
            for (Cafe in listCafe) {
                if (Cafe.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    filteredList.add(Cafe)
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
        for (Cafe in listCafe) {
            if (Cafe.recommendation=="yes") {
                filteredList.add(Cafe)
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

    private fun generateSongList(): ArrayList<Cafe> {
        val songNames = resources.getStringArray(R.array.data_cafe)
        val songDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val songImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val songAudios = resources.obtainTypedArray(R.array.data_audio)
        val category = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)


        val cafeList = ArrayList<Cafe>()
        for (i in songNames.indices) {
            val cafe = Cafe(
                songNames[i],
                songDescriptions[i],
                songImages.getResourceId(i, -1),
                songAudios.getResourceId(i, -1),
                category[i],
                recommended[i]
            )
            cafeList.add(cafe)
        }
        songImages.recycle()
        songAudios.recycle()

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
        when (item.itemId) {
            R.id.action_list -> {
                binding.rvCafe.layoutManager = LinearLayoutManager(context)
                return true
            }

            R.id.action_grid -> {
                binding.rvCafe.layoutManager = GridLayoutManager(context, 2)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}

