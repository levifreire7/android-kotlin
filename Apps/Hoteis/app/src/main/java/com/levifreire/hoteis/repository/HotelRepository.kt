package com.levifreire.hoteis.repository

import androidx.lifecycle.LiveData
import com.levifreire.hoteis.model.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun remove(vararg hotels: Hotel)
    fun hotelById(id: Long): LiveData<Hotel>
    fun search(term: String): LiveData<List<Hotel>>
}