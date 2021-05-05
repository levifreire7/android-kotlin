package com.levifreire.navigationapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnGo = view.findViewById<Button>(R.id.btnGo)

        btnGo.setOnClickListener {
            val args = Bundle().apply {
                putString("full_name", "Fulano de Tal")
                putInt("age", 38)
            }
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_homeFragment_to_completeFragment, args)
        }
    }
}