package com.levifreire.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_dialog, null)
        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener { dialog.dismiss() }
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener { dialog.dismiss() }
        dialog.setContentView(view)
    }
}