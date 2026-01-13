package com.example.pinlite

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pinlite.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var uid: String
    private lateinit var userRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        uid = auth.currentUser?.uid ?: return
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

        loadUserProfile()

        binding.btnMore.setOnClickListener { v ->
            val popup = android.widget.PopupMenu(requireContext(), v)
            popup.menu.add("Edit Profil")
            popup.menu.add("Logout")
            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    "Edit Profil" -> {
                        startActivity(Intent(requireContext(), EditProfileActivity::class.java))
                        true
                    }
                    "Logout" -> {
                        auth.signOut()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun loadUserProfile() {
        userRef.get().addOnSuccessListener {
            binding.tvUsername.text = it.child("username").value.toString()
            binding.tvEmail.text = auth.currentUser?.email ?: "Email tidak tersedia"

            val base64Image = it.child("profileImageBase64").value?.toString()
            if (!base64Image.isNullOrEmpty()) {
                try {
                    val bytes = Base64.decode(base64Image, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    binding.imgProfil.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.imgProfil.setImageResource(R.drawable.bg_circle)
                }
            } else {
                binding.imgProfil.setImageResource(R.drawable.bg_circle)
            }
        }.addOnFailureListener {
            binding.imgProfil.setImageResource(R.drawable.bg_circle)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh profil setiap kali kembali dari EditProfileActivity
        loadUserProfile()
    }
}
