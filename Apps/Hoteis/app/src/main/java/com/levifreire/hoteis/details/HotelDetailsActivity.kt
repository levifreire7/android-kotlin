package com.levifreire.hoteis.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.levifreire.hoteis.R
import com.levifreire.hoteis.form.HotelFormFragment
import com.levifreire.hoteis.model.Hotel

class HotelDetailsActivity : AppCompatActivity() {
    private val hotelId: Long by lazy { intent.getLongExtra(EXTRA_HOTEL_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)
        if (savedInstanceState == null) {
            showHotelDetailsFragment()
        }
    }

    private fun showHotelDetailsFragment() {
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

    companion object {
        private const val EXTRA_HOTEL_ID = "hotel_id"
        fun open(activity: Activity, hotelId: Long) {
            activity.startActivityForResult(
                Intent(
                    activity,
                    HotelDetailsActivity::class.java
                ).apply {
                    putExtra(EXTRA_HOTEL_ID, hotelId)
                }, 0
            )
        }
    }
}