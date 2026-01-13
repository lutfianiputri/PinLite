package com.example.pinlite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinlite.model.Pin

class HomeViewModel : ViewModel() {

    private val _uploadedPins = MutableLiveData<MutableList<Pin>>(mutableListOf())
    val uploadedPins: LiveData<MutableList<Pin>> = _uploadedPins

    fun addPin(pin: Pin) {
        _uploadedPins.value?.add(0, pin) // selalu tampil paling atas
        _uploadedPins.value = _uploadedPins.value
    }
}
