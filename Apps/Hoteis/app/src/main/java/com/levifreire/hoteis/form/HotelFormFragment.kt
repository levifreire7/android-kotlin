package com.levifreire.hoteis.form

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.levifreire.hoteis.model.Hotel
import com.levifreire.hoteis.R
import com.levifreire.hoteis.databinding.FragmentHotelFormBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HotelFormFragment : DialogFragment() {
    private val viewModel: HotelFormViewModel by viewModel()
    private var fragmentHotelFormBinding: FragmentHotelFormBinding? = null
    private var hotel: Hotel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hotel_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHotelFormBinding.bind(view)
        fragmentHotelFormBinding = binding

        val hotelId = arguments?.getLong(EXTRA_HOTEL_ID, 0) ?: 0

        if (hotelId > 0) {
            viewModel.loadHotel(hotelId).observe(viewLifecycleOwner, Observer { hotel ->
                this.hotel = hotel
                showHotel(hotel)
            })
        }

        fragmentHotelFormBinding!!.edtAddress.setOnEditorActionListener { _, i, _ ->
            handleKeyboardEvent(i)
        }
        dialog?.setTitle(R.string.action_new_hotel)
        //abre o teclado ao abrir o Dialog
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

    }

    private fun showHotel(hotel: Hotel) {
        fragmentHotelFormBinding?.edtName?.setText(hotel.name)
        fragmentHotelFormBinding?.edtAddress?.setText(hotel.address)
        fragmentHotelFormBinding?.rtbRating?.rating = hotel.rating
    }

    private fun errorInvalidHotel() {
        Toast.makeText(requireContext(), R.string.error_invalid_hotel, Toast.LENGTH_SHORT).show()
    }

    private fun errorSaveHotel() {
        Toast.makeText(requireContext(), R.string.error_hotel_not_found, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        fragmentHotelFormBinding = null
        super.onDestroyView()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            saveHotel()
            return true
        }
        return false
    }

    private fun saveHotel() {
        val hotel = this.hotel ?: Hotel()
        val hotelId = arguments?.getLong(EXTRA_HOTEL_ID, 0) ?: 0
        hotel.id = hotelId
        hotel.name = fragmentHotelFormBinding?.edtName?.text.toString()
        hotel.address = fragmentHotelFormBinding?.edtAddress?.text.toString()
        hotel.rating = fragmentHotelFormBinding?.rtbRating?.rating!!
        try {
            if (viewModel.saveHotel(hotel)) {
                dialog?.dismiss()
            } else {
                errorInvalidHotel()
            }
        } catch (e: Exception) {
            Log.i("HotelFormFragment", e.message.toString())
            errorSaveHotel()
        }
    }

    fun open(fm: FragmentManager) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG)
        }
    }

    companion object {
        private const val DIALOG_TAG = "editDialog"
        private const val EXTRA_HOTEL_ID = "hotel_id"

        fun newInstance(hotelId: Long = 0) = HotelFormFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, hotelId)
            }
        }
    }
}