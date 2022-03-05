package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Modify_group_detail1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_invite_detail_group)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }

        val saveButton = findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }
    }
}