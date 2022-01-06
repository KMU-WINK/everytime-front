package com.wink.knockmate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment_settings(context: Context) : BottomSheetDialogFragment() {

    interface OnDataPassListener{
        fun onDataPass(data : String?)
    }

    private lateinit var dataPassListener : OnDataPassListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as OnDataPassListener // 형변환
    }

    override fun getTheme(): Int = R.style.BottomSheetStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_profileimage_bottom_sheet,  container, false)
        val selectPhoto : Button = view.findViewById(R.id.selectPhoto)
        val deletePhoto : Button = view.findViewById(R.id.deletePhoto)
        val cancelButton : Button = view.findViewById(R.id.cancel_button)

        selectPhoto.setOnClickListener {
            // TODO 앨범에서 이미지를 선택한다.
            dataPassListener.onDataPass("Hello")
        }

        deletePhoto.setOnClickListener {
            // TODO 프로필 사진을 삭제한다.
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }
}