package com.levifreire.hoteis.imagefiles

import com.levifreire.hoteis.model.Hotel

interface FindHotelPicture {
    fun pictureFile(hotel: Hotel): PictureToUpload
}