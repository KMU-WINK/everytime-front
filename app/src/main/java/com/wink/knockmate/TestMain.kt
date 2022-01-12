package com.wink.knockmate

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TestMain : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener(View.OnClickListener {
            AddScheduleInfo.resetStartCal()
            val bottomSheetDialogFragment = AddSchedule()
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        })
    }
}