package com.levifreire.hoteis.di

import com.levifreire.hoteis.details.HotelDetailsViewModel
import com.levifreire.hoteis.form.HotelFormViewModel
import com.levifreire.hoteis.list.HotelListViewModel
import com.levifreire.hoteis.repository.HotelRepository
import com.levifreire.hoteis.repository.room.HotelDatabase
import com.levifreire.hoteis.repository.room.RoomRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    single { this }
    single {
        RoomRepository(HotelDatabase.getDatabase(context = get())) as HotelRepository
    }
    viewModel {
        HotelListViewModel(repository = get())
    }
    viewModel {
        HotelDetailsViewModel(repository = get())
    }
    viewModel {
        HotelFormViewModel(repository = get())
    }
}