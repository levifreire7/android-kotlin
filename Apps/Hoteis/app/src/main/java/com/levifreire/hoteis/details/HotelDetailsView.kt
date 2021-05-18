package com.levifreire.hoteis.details

import com.levifreire.hoteis.model.Hotel

interface HotelDetailsView {
    fun showHotelDetails(hotel: Hotel)
    fun errorHotelNotFound()
}