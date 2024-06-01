package com.projectpab.kelompok3.cafefinder.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.projectpab.kelompok3.cafefinder.R
import com.projectpab.kelompok3.cafefinder.databinding.FragmentDashboardBinding
import com.projectpab.kelompok3.cafefinder.Cafe

class CafeFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListCafeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ListCafeAdapter(generateSongList())
        binding.rvSong.adapter = adapter
        binding.rvSong.layoutManager = LinearLayoutManager(context)

        return root
    }

    private fun generateSongList(): ArrayList<Cafe> {
        val songNames = resources.getStringArray(R.array.data_name)
        val songDescriptions = resources.getStringArray(R.array.data_desc)
        val songImages = resources.obtainTypedArray(R.array.data_img)
        val songAudios = resources.obtainTypedArray(R.array.data_audio)

        val cafeList = ArrayList<Cafe>()
        for (i in songNames.indices) {
            val cafe = Cafe(songNames[i], songDescriptions[i], songImages.getResourceId(i, -1), songAudios.getResourceId(i, -1))
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
                binding.rvSong.layoutManager = LinearLayoutManager(context)
                return true
            }

            R.id.action_grid -> {
                binding.rvSong.layoutManager = GridLayoutManager(context, 2)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}