package com.techventure.androidext.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

abstract class DialogUtils : DialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.apply { window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
    }
}