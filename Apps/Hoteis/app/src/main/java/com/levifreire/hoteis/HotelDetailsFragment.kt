package com.levifreire.hoteis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levifreire.hoteis.databinding.FragmentHotelDetailsBinding

class HotelDetailsFragment : Fragment(), HotelDetailsView {
    private val presenter = HotelDetailsPresenter(this, MemoryRepository)
    private var fragmentHotelDetailsBinding: FragmentHotelDetailsBinding? = null
    private var hotel: Hotel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hotel_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHotelDetailsBinding.bind(view)
        fragmentHotelDetailsBinding = binding

        presenter.loadHotelDetails(arguments?.getLong(EXTRA_HOTEL_ID, -1) ?: -1)
    }

    override fun showHotelDetails(hotel: Hotel) {
        this.hotel = hotel
        fragmentHotelDetailsBinding?.txtName?.text = hotel.name
        fragmentHotelDetailsBinding?.txtAddress?.text = hotel.address
        fragmentHotelDetailsBinding?.rtbRating?.rating = hotel.rating
    }

    override fun errorHotelNotFound() {
        fragmentHotelDetailsBinding?.txtName?.text = getString(R.string.error_hotel_not_found)
        fragmentHotelDetailsBinding?.txtAddress?.visibility = View.GONE
        fragmentHotelDetailsBinding?.rtbRating?.visibility = View.GONE
    }

    override fun onDestroyView() {
        fragmentHotelDetailsBinding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG_DETAILS = "tagDetalhe"
        private const val EXTRA_HOTEL_ID = "hotelId"

        fun newInstance(id: Long) = HotelDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, id)
            }
        }
    }
}