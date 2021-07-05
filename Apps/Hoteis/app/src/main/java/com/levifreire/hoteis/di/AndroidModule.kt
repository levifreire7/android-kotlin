package com.levifreire.hoteis.di

import com.google.gson.GsonBuilder
import com.levifreire.hoteis.details.HotelDetailsViewModel
import com.levifreire.hoteis.form.HotelFormViewModel
import com.levifreire.hoteis.list.HotelListViewModel
import com.levifreire.hoteis.repository.HotelRepository
import com.levifreire.hoteis.repository.http.HotelHttp
import com.levifreire.hoteis.repository.http.HotelHttpApi
import com.levifreire.hoteis.repository.room.HotelDatabase
import com.levifreire.hoteis.repository.room.RoomRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val androidModule = module {
    single { this }
    single {
        RoomRepository(HotelDatabase.getDatabase(context = get())) as HotelRepository
    }
    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(HotelHttp.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
        retrofit.create<HotelHttpApi>(HotelHttpApi::class.java)
    }
    factory {
        HotelHttp(service = get(), repository = get(), currentUser = "levi")
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