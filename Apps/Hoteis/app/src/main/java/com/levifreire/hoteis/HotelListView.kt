package com.levifreire.hoteis

interface HotelListView {
    fun showHotels(hotels: List<Hotel>)
    fun showHotelDetails(hotel: Hotel)
}