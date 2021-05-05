package com.levifreire.navigationapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CompleteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
        arguments?.run {
            val fullName = getString("full_name")
            val age = getInt("age")
            txtMessage.text = "$fullName - $age"
        }

    }
}