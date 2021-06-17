package com.levifreire.hoteis.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.repository.sqlite.DATABASE_NAME
import com.levifreire.hoteis.repository.sqlite.DATABASE_VERSION

@Database(entities = [Hotel::class], version = DATABASE_VERSION)
abstract class HotelDatabase : RoomDatabase() {

    abstract val hotelDao: HotelDao

    companion object {

        @Volatile
        private var INSTANCE: HotelDatabase? = null

        fun getDatabase(context: Context): HotelDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HotelDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}