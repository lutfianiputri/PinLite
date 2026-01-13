package com.example.pinlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pinlite.databinding.FragmentHomeBinding
import com.example.pinlite.model.Pin

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val pinList = mutableListOf<Pin>()
    private lateinit var pinAdapter: PinAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // GRID 2 KOLOM
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // ===============================
        // DATA DEFAULT
        // ===============================
        pinList.clear()
        addDefaultPins()

        // ===============================
        // ADAPTER
        // ===============================
        pinAdapter = PinAdapter(
            pinList,
            listener = { pin ->
                val intent = Intent(requireContext(), DetailPinActivity::class.java)
                intent.putExtra("title", pin.title)
                if (pin.imageRes != null) {
                    intent.putExtra("image", pin.imageRes)
                } else if (pin.imageUri != null) {
                    intent.putExtra("imageUri", pin.imageUri.toString())
                }
                startActivity(intent)
            },
            onLongClick = null
        )

        binding.recyclerView.adapter = pinAdapter
        pinAdapter.updateList(pinList) // 🔴 WAJIB → BIAR GAMBAR MUNCUL

        // ===============================
        // SEARCH VIEW (LANGSUNG TERLIHAT)
        // ===============================
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()

        val searchEditText =
            binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.hint = "Cari inspirasi"
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.searchView.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_search)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pinAdapter.filter(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pinAdapter.filter(newText ?: "")
                return true
            }
        })

        // ===============================
        // VIEWMODEL (UPLOAD DARI AddFragment)
        // ===============================
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        viewModel.uploadedPins.observe(viewLifecycleOwner) { uploadedPins ->
            pinList.clear()
            addDefaultPins()
            pinList.addAll(0, uploadedPins)
            pinAdapter.updateList(pinList) // 🔴 WAJIB
        }
    }

    private fun addDefaultPins() {
        pinList.add(Pin("Inspirasi Kamar", R.drawable.kamar1))
        pinList.add(Pin("Inspirasi Kamar", R.drawable.kamar2))
        pinList.add(Pin("Inspirasi Kamar", R.drawable.kamar3))
        pinList.add(Pin("Inspirasi Kamar", R.drawable.kamar4))
        pinList.add(Pin("Lukisan", R.drawable.lukisan1))
        pinList.add(Pin("Lukisan", R.drawable.lukisan2))
        pinList.add(Pin("Lukisan", R.drawable.lukisan3))
        pinList.add(Pin("Lukisan", R.drawable.lukisan4))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper1))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper2))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper3))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper4))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper5))
        pinList.add(Pin("Wallpaper Aesthetic", R.drawable.wallpaper6))
        pinList.add(Pin("Inspirasi Outfit", R.drawable.outfit1))
        pinList.add(Pin("Inspirasi Outfit", R.drawable.outfit2))
        pinList.add(Pin("Inspirasi Outfit", R.drawable.outfit3))
        pinList.add(Pin("Inspirasi Outfit", R.drawable.outfit4))
        pinList.add(Pin("Desain Poster", R.drawable.poster1))
        pinList.add(Pin("Desain Poster", R.drawable.poster2))
        pinList.add(Pin("Desain Poster", R.drawable.poster3))
        pinList.add(Pin("Desain Poster", R.drawable.poster4))
        pinList.add(Pin("Desain Poster", R.drawable.poster5))
        pinList.add(Pin("Desain Poster", R.drawable.poster6))
        pinList.add(Pin("Kombinasi OOTD Hijab", R.drawable.ootd))
        pinList.add(Pin("Inspirasi Gaya Berfoto", R.drawable.gaya1))
        pinList.add(Pin("Inspirasi Gaya Berfoto", R.drawable.gaya2))
        pinList.add(Pin("Inspirasi Gaya Berfoto", R.drawable.gaya3))
        pinList.add(Pin("UI Mobile App", R.drawable.ui1))
        pinList.add(Pin("UI Mobile App", R.drawable.ui2))
        pinList.add(Pin("UI Mobile App", R.drawable.ui3))
        pinList.add(Pin("UI Mobile App", R.drawable.ui4))
        pinList.add(Pin("Quote Motivasi", R.drawable.quote1))
        pinList.add(Pin("Quote Motivasi", R.drawable.quote2))
        pinList.add(Pin("Quote Motivasi", R.drawable.quote3))
        pinList.add(Pin("Quote Motivasi", R.drawable.quote4))
        pinList.add(Pin("OOTD Pria", R.drawable.ootdpria1))
        pinList.add(Pin("OOTD Pria", R.drawable.ootdpria2))
        pinList.add(Pin("OOTD Pria", R.drawable.ootdpria3))
        pinList.add(Pin("OOTD Pria", R.drawable.ootdpria4))
    }
}
