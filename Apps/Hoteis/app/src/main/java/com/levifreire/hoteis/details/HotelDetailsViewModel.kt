package com.levifreire.hoteis.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.repository.HotelRepository

class HotelDetailsViewModel(private val repository: HotelRepository) : ViewModel() {
    fun loadHotelDetails(id: Long): LiveData<Hotel> {
        return repository.hotelById(id)
    }
}