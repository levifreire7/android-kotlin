package com.levifreire.hoteis.details

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.R
import com.levifreire.hoteis.databinding.FragmentHotelDetailsBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HotelDetailsFragment : Fragment(), HotelDetailsView {
    private val presenter: HotelDetailsPresenter by inject { parametersOf(this) }
    private var fragmentHotelDetailsBinding: FragmentHotelDetailsBinding? = null
    private var hotel: Hotel? = null
    private var shareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.hotel_details, menu)
        val shareItem = menu.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as? ShareActionProvider
        setShareIntent()
    }

    private fun setShareIntent() {
        val text = getString(R.string.share_text, hotel?.name, hotel?.rating)
        shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        })
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