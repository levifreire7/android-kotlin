package com.levifreire.hoteis

interface HotelDetailsView {
    fun showHotelDetails(hotel: Hotel)
    fun errorHotelNotFound()
}