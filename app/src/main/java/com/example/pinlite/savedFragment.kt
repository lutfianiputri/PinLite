package com.example.pinlite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pinlite.databinding.FragmentSavedBinding
import com.example.pinlite.model.Pin

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var adapter: PinAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSavedBinding.bind(view)

        binding.recyclerViewSaved.layoutManager =
            GridLayoutManager(requireContext(), 2)

        adapter = PinAdapter(
            pinList = SavedPinStore.savedPins,
            listener = { /* klik biasa (opsional) */ },
            onLongClick = { pin, _ ->   // 🔥 WAJIB 2 PARAMETER
                showDeleteDialog(pin)
            }
        )

        binding.recyclerViewSaved.adapter = adapter
    }

    private fun showDeleteDialog(pin: Pin) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pin")
            .setMessage("Yakin ingin menghapus pin ini?")
            .setPositiveButton("Hapus") { _, _ ->
                // ✅ HAPUS DARI DATA
                SavedPinStore.savedPins.remove(pin)

                // ✅ REFRESH ADAPTER (WAJIB)
                adapter.updateList(SavedPinStore.savedPins)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
