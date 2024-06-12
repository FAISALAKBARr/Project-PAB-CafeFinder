package com.projectpab.kelompok3.cafefinder.ui.home

import android.annotation.SuppressLint
import android.content.Context
import com.projectpab.kelompok3.cafefinder.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projectpab.kelompok3.cafefinder.Cafe
import com.projectpab.kelompok3.cafefinder.FavoriteManager
import com.projectpab.kelompok3.cafefinder.databinding.FragmentHomeBinding
import com.projectpab.kelompok3.cafefinder.ui.favorite.ListFavoriteAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RandomizedCafeAdapter
    private lateinit var listCafe: ArrayList<Cafe>
    private lateinit var randomizedIndex: IntArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US)
        if (dateFormat is SimpleDateFormat) {
            dateFormat.applyPattern("EEEE, dd MMMM yyyy")
        }
        val date = dateFormat.format(Date())
        val dispDate = binding.date
        dispDate.text = date
        updateFavoriteCount()

        /*val sb = StringBuilder()
        for (number in randomizedIndex) {
            sb.append(number)
            sb.append(", ")
        }
        val result = sb.dropLast(2).toString() // Remove the last ", "
        dispIndex.text = result*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomizedIndex = RandomizeManager.getStoredRandomNumbers(requireContext())
        // dispIndex = binding.substitute
        Log.d("Debug", randomizedIndex.contentToString())

        val recyclerView: RecyclerView = view?.findViewById(R.id.recycle_view) ?: throw IllegalStateException("RecyclerView not found")
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        listCafe = generateCafeList(randomizedIndex)
        adapter = RandomizedCafeAdapter(listCafe)
        recyclerView.adapter = adapter
        updateCafeRatings()
    }

    fun generateCafeList(indexes: IntArray): ArrayList<Cafe> {
        val cafeNames = resources.getStringArray(R.array.data_cafe)
        val cafeDescriptions = resources.getStringArray(R.array.data_desc_cafe)
        val cafeImages = resources.obtainTypedArray(R.array.data_img_cafe)
        val categories = resources.getStringArray(R.array.data_cash)
        val recommended = resources.getStringArray(R.array.data_recommended)

        val cafeList = ArrayList<Cafe>()
        for (i in indexes) {
            val cafe = Cafe(
                cafeNames[i],
                cafeDescriptions[i],
                cafeImages.getResourceId(i, -1),
                categories[i],
                recommended[i],
                0f, // Default rating, it will be updated later
            )
            cafeList.add(cafe)
        }
        cafeImages.recycle()
        return cafeList
    }

    override fun onResume() {
        super.onResume()
        updateCafeRatings()
        updateFavoriteCount()
    }

    private fun updateFavoriteCount() {
        val favoriteCount = FavoriteManager.getFavoriteCount(requireContext())
        val dispFavCount = binding.favCount
        dispFavCount.text = favoriteCount.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCafeRatings() {
        val sharedPreferences = requireContext().getSharedPreferences("CafeFinderPrefs", Context.MODE_PRIVATE)
        for (cafe in listCafe) {
            val rating = sharedPreferences.getFloat(cafe.name, 0f)
            cafe.rating = rating
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}