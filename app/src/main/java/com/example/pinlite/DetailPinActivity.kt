package com.example.pinlite

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pinlite.databinding.ActivityDetailPinBinding
import com.example.pinlite.model.Pin

class DetailPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val title = intent.getStringExtra("title") ?: ""
        val imageRes = intent.getIntExtra("image", 0)
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        // Set title
        binding.tvTitle.text = title

        // Tampilkan gambar
        when {
            imageRes != 0 -> {
                // Gambar dari drawable
                binding.imgPin.setImageResource(imageRes)
            }
            imageUri != null -> {
                // Gambar dari URI (galeri)
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.imgPin)
            }
            else -> {
                // Placeholder jika tidak ada gambar
                binding.imgPin.setImageResource(R.drawable.bg_placeholder)
            }
        }

        // Tombol Simpan
        binding.btnSave.setOnClickListener {
            val pinToSave = if (imageRes != 0) {
                Pin(title, imageRes) // Simpan drawable
            } else if (imageUri != null) {
                Pin(title, imageUri = imageUri) // Simpan URI
            } else {
                Toast.makeText(this, "Tidak ada gambar untuk disimpan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SavedPinStore.savedPins.add(pinToSave)
            Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
        }
    }
}
