package com.levifreire.hoteis.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.levifreire.hoteis.R
import com.levifreire.hoteis.model.Hotel

class HotelAdapter(context: Context, hotels: List<Hotel>) :
    ArrayAdapter<Hotel>(context, 0, hotels) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val hotel = getItem(position)
        val viewHolder = if (convertView == null) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotel, parent, false)
            val holder = ViewHolder(view)
            view.tag = holder
            holder
        } else {
            convertView.tag as ViewHolder
        }
        viewHolder.txtName.text = hotel?.name
        viewHolder.rtbRating.rating = hotel?.rating ?: 0f
        return viewHolder.view
    }

    class ViewHolder(val view: View) {
        val txtName: TextView = view.findViewById(R.id.txtName)
        val rtbRating: RatingBar = view.findViewById(R.id.rtbRating)
    }
}