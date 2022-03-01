package com.techventure.androidbase.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.techventure.androidext.ext.getScreenSize
import com.techventure.androidext.ext.setOnGlobalLayoutChangeListener
import com.techventure.androidext.ext.setViewSize
import com.techventure.androidext.utils.ExtConstant.ONE
import com.techventure.androidext.utils.ExtConstant.ZERO
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomSheetUtils : BottomSheetDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    fun setDialogCancelable(shouldCancel:Boolean=false){
        isCancelable = shouldCancel
    }

    fun ViewBinding.setBottomSheetBehaviourState(behaviorState:Int?=null,shouldDrag:Boolean=true){
        BottomSheetBehavior.from(root.parent as View).apply {
            if (behaviorState != null) {
                state = behaviorState
            }
            isDraggable = shouldDrag
        }
    }

    fun ViewBinding.setBottomSheetHeight() {
        root.setOnGlobalLayoutChangeListener {
            if (activity == null || activity?.isFinishing == true) return@setOnGlobalLayoutChangeListener
            activity?.getScreenSize()?.apply {
                root.setViewSize(
                    get(ZERO),
                    get(ONE)
                )
            }
        }
    }
}