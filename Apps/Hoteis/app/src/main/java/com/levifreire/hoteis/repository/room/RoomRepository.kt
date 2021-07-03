package com.levifreire.hoteis.repository.room

import androidx.lifecycle.LiveData
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.repository.HotelRepository
import com.levifreire.hoteis.repository.http.Status

class RoomRepository(database: HotelDatabase) : HotelRepository {

    private val hotelDao = database.hotelDao

    override fun save(hotel: Hotel) {
        if (hotel.id == 0L) {
            hotel.status = Status.INSERT
            val id = insert(hotel)
            hotel.id = id
        } else {
            hotel.status = Status.UPDATE
            update(hotel)
        }
    }

    override fun insert(hotel: Hotel): Long {
        return hotelDao.insert(hotel)
    }

    override fun update(hotel: Hotel) {
        hotelDao.update(hotel)
    }

    override fun remove(vararg hotels: Hotel) {
        hotelDao.delete(*hotels)
    }

    override fun hotelById(id: Long): LiveData<Hotel> {
        return hotelDao.hotelById(id)
    }

    override fun search(term: String): LiveData<List<Hotel>> {
        return hotelDao.search(term)
    }

    override fun pending(): List<Hotel> {
        return hotelDao.pending()
    }

    override fun hotelByServerId(serverId: Long): Hotel? {
        return hotelDao.hotelByServerId(serverId)
    }
}