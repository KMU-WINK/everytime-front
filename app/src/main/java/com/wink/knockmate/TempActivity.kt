package com.wink.knockmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TempActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        findViewById<Button>(R.id.temp_login).setOnClickListener {
            startActivity(Intent(this@TempActivity, LogInActivity::class.java))
        }
        findViewById<Button>(R.id.temp_signup).setOnClickListener {
            startActivity(Intent(this@TempActivity, SignUpActivity::class.java))
        }
    }
}