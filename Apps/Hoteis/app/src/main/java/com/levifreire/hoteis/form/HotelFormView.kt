package com.levifreire.hoteis.form

import com.levifreire.hoteis.model.Hotel

interface HotelFormView {
    fun showHotel(hotel: Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}