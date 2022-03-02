package com.wink.knockmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val intent = Intent(this,TutorialActivity2::class.java)
        bt_tutorial_1p.setOnClickListener {
            startActivity(intent)
        }

    }
}