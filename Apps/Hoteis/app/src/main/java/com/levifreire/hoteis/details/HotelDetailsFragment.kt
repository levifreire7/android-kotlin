package com.levifreire.hoteis.details

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.R
import com.levifreire.hoteis.databinding.FragmentHotelDetailsBinding
import com.levifreire.hoteis.form.HotelFormFragment
import com.levifreire.hoteis.repository.http.HotelHttp
import org.koin.android.viewmodel.ext.android.viewModel

class HotelDetailsFragment : Fragment() {
    private val viewModel: HotelDetailsViewModel by viewModel()
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

        val id = arguments?.getLong(EXTRA_HOTEL_ID, -1) ?: -1
        viewModel.loadHotelDetails(id).observe(viewLifecycleOwner, Observer { hotel ->
            if (hotel != null) {
                showHotelDetails(hotel)
            } else {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
                errorHotelNotFound()
            }
        })
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

    private fun showHotelDetails(hotel: Hotel) {
        this.hotel = hotel
        fragmentHotelDetailsBinding?.txtName?.text = hotel.name
        fragmentHotelDetailsBinding?.txtAddress?.text = hotel.address
        fragmentHotelDetailsBinding?.rtbRating?.rating = hotel.rating
        var photoUrl = hotel.photoUrl
        if (photoUrl.isNotEmpty()) {
            if (!photoUrl.contains("content://")) {
                photoUrl = HotelHttp.BASE_URL + hotel.photoUrl
            }
            Glide.with(fragmentHotelDetailsBinding!!.imgPhoto.context).load(photoUrl)
                .into(fragmentHotelDetailsBinding!!.imgPhoto)
        }
    }

    private fun errorHotelNotFound() {
        fragmentHotelDetailsBinding?.txtName?.text = getString(R.string.error_hotel_not_found)
        fragmentHotelDetailsBinding?.txtAddress?.visibility = View.GONE
        fragmentHotelDetailsBinding?.rtbRating?.visibility = View.GONE
    }

    override fun onDestroyView() {
        fragmentHotelDetailsBinding = null
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            HotelFormFragment
                .newInstance(hotel?.id ?: 0)
                .open(parentFragmentManager)
        }
        return super.onOptionsItemSelected(item)
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