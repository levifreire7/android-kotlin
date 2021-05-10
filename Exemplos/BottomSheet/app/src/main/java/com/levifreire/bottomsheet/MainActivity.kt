package com.levifreire.bottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.levifreire.bottomsheet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val layoutBottomSheet = findViewById<LinearLayout>(R.id.layoutBottomSheet)

        binding.btnBottomSheet.setOnClickListener {
            val behavior = BottomSheetBehavior.from(layoutBottomSheet)
            if (behavior.state == BottomSheetBehavior.STATE_HIDDEN || behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}