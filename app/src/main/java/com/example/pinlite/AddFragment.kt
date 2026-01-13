package com.example.pinlite

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pinlite.databinding.FragmentAddBinding
import com.example.pinlite.model.Pin

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private var imageUri: Uri? = null
    private lateinit var viewModel: HomeViewModel

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.imgPreview.setImageURI(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnUpload.setOnClickListener {
            if (imageUri != null) {
                val caption = binding.etCaption.text.toString().ifEmpty { "Tidak ada caption" }
                val pin = Pin(caption, imageUri = imageUri)
                viewModel.addPin(pin)

                Toast.makeText(requireContext(), "Foto berhasil ditambahkan ke Home", Toast.LENGTH_SHORT).show()
                binding.imgPreview.setImageDrawable(null)
                binding.etCaption.text.clear()
            } else {
                Toast.makeText(requireContext(), "Pilih foto dulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
