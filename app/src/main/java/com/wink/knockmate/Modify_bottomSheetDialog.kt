package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.modify_bottomsheet.view.*

class Modify_bottomSheetDialog(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.modify_bottomsheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.cancel_button.setOnClickListener {
            itemClick(0)
            dialog?.dismiss()
        }
        view.reknock_button.setOnClickListener {
            itemClick(1)
            dialog?.dismiss()
        }
    }
}