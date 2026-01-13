package com.example.pinlite.model

import android.net.Uri

data class Pin(
    val title: String,
    val imageRes: Int? = null,   // Untuk gambar default dari drawable
    val imageUri: Uri? = null    // Untuk gambar dari galeri / AddFragment
)
