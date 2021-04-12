package com.levifreire.hoteis

interface HotelFormView {
    fun showHotel(hotel: Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}