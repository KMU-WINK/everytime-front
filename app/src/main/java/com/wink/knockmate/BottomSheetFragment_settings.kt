package com.wink.knockmate

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.app.AlertDialog

import android.content.ActivityNotFoundException
import android.provider.Settings


class BottomSheetFragment_settings(context: Context) : BottomSheetDialogFragment() {

    private val _context = context

    interface OnDataPassListener{
        fun onDataPass(data : Uri?, imageFlag : Boolean)
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
            when{
                ContextCompat.checkSelfPermission(_context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2000)
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2000)
                }
            }
        }

        deletePhoto.setOnClickListener {
            val uri : Uri = Uri.parse("android.resource://com.wink.knockmate/drawable/default_profile")
            dataPassListener.onDataPass(uri, false)
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            2000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    navigatePhotos()
                } else{
                    Toast.makeText(_context, "권한을 거절하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigatePhotos(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode){
            1000 -> {
                val selectedImageUri : Uri? = data?.data
                if(selectedImageUri != null){
                    dataPassListener.onDataPass(selectedImageUri, true)
                    dismiss()
                } else{
                    Toast.makeText(_context, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else -> {
                Toast.makeText(_context, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}