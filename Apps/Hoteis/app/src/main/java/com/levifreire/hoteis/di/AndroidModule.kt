package com.levifreire.hoteis.di

import com.levifreire.hoteis.details.HotelDetailsPresenter
import com.levifreire.hoteis.details.HotelDetailsView
import com.levifreire.hoteis.form.HotelFormPresenter
import com.levifreire.hoteis.form.HotelFormView
import com.levifreire.hoteis.list.HotelListPresenter
import com.levifreire.hoteis.list.HotelListView
import com.levifreire.hoteis.repository.HotelRepository
import com.levifreire.hoteis.repository.sqlite.SQLiteRepository
import org.koin.dsl.module

val androidModule = module {
    single { this }
    single {
        SQLiteRepository(ctx = get()) as HotelRepository
    }
    factory { (view: HotelListView) ->
        HotelListPresenter(
            view,
            repository = get()
        )
    }
    factory { (view: HotelDetailsView) ->
        HotelDetailsPresenter(
            view,
            repository = get()
        )
    }
    factory { (view: HotelFormView) ->
        HotelFormPresenter(
            view,
            repository = get()
        )
    }
}