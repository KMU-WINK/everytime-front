package com.wink.knockmate

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class InquiryActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry)

        backButton.setOnClickListener {
            finish()
        }
    }
}