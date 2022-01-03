package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private val editProfileButton : Button = findViewById(R.id.editProfileButton)

    private val editEmailButton : Button = findViewById(R.id.changeEmail)

    private val editPasswordButton : Button = findViewById(R.id.changePassword)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        editProfileButton.setOnClickListener {
            initEditProfileButton()
        }

        editEmailButton.setOnClickListener {
            initEditEmailButton()
        }

        editPasswordButton.setOnClickListener {
            initEditPasswordButton()
        }
    }

    private fun initEditProfileButton(){
        val intent = Intent(this,EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun initEditEmailButton(){
        val intent = Intent(this,EditEmailActivity::class.java)
        startActivity(intent)
    }

    private fun initEditPasswordButton(){
        val intent = Intent(this,EditPasswordActivity::class.java)
        startActivity(intent)
    }
}