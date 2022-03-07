package com.wink.knockmate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.modify_cancel_dialog.view.*

class Modify_invite_dialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modify_cancel_dialog, container, false)
        val bundle = arguments
        val temp = bundle?.getString("name")
        view.findViewById<TextView>(R.id.dialog_text).text = temp + " 님께 보낸 노크를 취소할까요?"

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            buttonClickListener.onButton1Clicked()
            dismiss()
        }
        view.findViewById<Button>(R.id.knock_cancel_button).setOnClickListener {
            buttonClickListener.onButton2Clicked()
            dismiss()
        }

        return view
    }

    interface OnButtonClickListener {
        fun onButton1Clicked()
        fun onButton2Clicked()
    }

    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    private lateinit var buttonClickListener: OnButtonClickListener
}


//class Modify_invite_dialog(val itemClick: (Int) -> Unit) : DialogFragment() {
//    @SuppressLint("SetTextI18n")
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.modify_cancel_dialog, container, false)
//        val bundle = arguments
//        val temp = bundle?.getString("name")
//        view.findViewById<TextView>(R.id.dialog_text).text = temp + " 님께 보낸 노크를 취소할까요?"
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        view.cancel_button.setOnClickListener {
//            itemClick(0)
//            dialog?.dismiss()
//        }
//        view.knock_cancel_button.setOnClickListener {
//            itemClick(1)
//            dialog?.dismiss()
//        }
//    }
//
//}