package com.example.pinlite

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 100
    private var imageUri: Uri? = null

    private lateinit var imgProfile: ImageView
    private lateinit var tvChangePhoto: TextView
    private lateinit var tvDeletePhoto: TextView
    private lateinit var editUsername: EditText
    private lateinit var btnSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val userRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        imgProfile = findViewById(R.id.imgProfile)
        tvChangePhoto = findViewById(R.id.tvChangePhoto)
        tvDeletePhoto = findViewById(R.id.tvDeletePhoto)
        editUsername = findViewById(R.id.editUsername)
        btnSave = findViewById(R.id.btnSave)

        val uid = auth.currentUser?.uid ?: return

        // =======================
        // LOAD DATA USER
        // =======================
        userRef.child(uid).get().addOnSuccessListener {
            editUsername.setText(it.child("username").value?.toString() ?: "")

            val base64Image = it.child("profileImageBase64").value?.toString()
            if (!base64Image.isNullOrEmpty()) {
                val bytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgProfile.setImageBitmap(bitmap)
                tvDeletePhoto.visibility = View.VISIBLE
            } else {
                imgProfile.setImageResource(R.drawable.bg_circle)
                tvDeletePhoto.visibility = View.GONE
            }
        }

        // =======================
        // GANTI FOTO
        // =======================
        tvChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // =======================
        // HAPUS FOTO
        // =======================
        tvDeletePhoto.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Foto Profil")
                .setMessage("Yakin ingin menghapus foto profil?")
                .setPositiveButton("Hapus") { _, _ ->
                    userRef.child(uid).child("profileImageBase64").removeValue()
                    imgProfile.setImageResource(R.drawable.bg_circle)
                    imageUri = null
                    tvDeletePhoto.visibility = View.GONE
                    Toast.makeText(this, "Foto profil dihapus", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // =======================
        // SIMPAN
        // =======================
        btnSave.setOnClickListener {
            val username = editUsername.text.toString().trim()
            if (username.isEmpty()) {
                editUsername.error = "Username tidak boleh kosong"
                return@setOnClickListener
            }

            userRef.child(uid).child("username").setValue(username)

            if (imageUri != null) {
                val inputStream = contentResolver.openInputStream(imageUri!!)
                val bytes = inputStream!!.readBytes()
                val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                userRef.child(uid).child("profileImageBase64").setValue(base64)
            }

            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            Glide.with(this).load(imageUri).into(imgProfile)
            tvDeletePhoto.visibility = View.VISIBLE
        }
    }
}
