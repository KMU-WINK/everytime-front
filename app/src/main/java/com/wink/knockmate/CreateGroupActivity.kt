package com.wink.knockmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CreateGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        findViewById<EditText>(R.id.create_group_edittext).background.clearColorFilter();
        findViewById<Button>(R.id.btGroupCreateNext).setOnClickListener {
            val intent = Intent(this, InviteGroupMemActivity::class.java)
            startActivity(intent)
        }

    }
}