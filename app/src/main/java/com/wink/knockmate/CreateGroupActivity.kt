package com.wink.knockmate

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton

class CreateGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        val next = findViewById<AppCompatButton>(R.id.btGroupCreateNext)
        var createable = false
        val editText = findViewById<EditText>(R.id.create_group_edittext)
        editText.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                createable = if (charSequence!!.length >= 2) {
                    next.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.brand_main))
                    true
                } else {
                    next.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.disabled))
                    false
                }
            }

            override fun afterTextChanged(charSequence: Editable?) {
            }
        })
        editText.background.clearColorFilter();
        next.setOnClickListener {
            if (createable) {
                val intent = Intent(this, InviteGroupMemActivity::class.java)
                intent.putExtra("nickname", editText.text.toString())
                startActivity(intent)
            }
        }
        findViewById<ImageButton>(R.id.create_group_back).setOnClickListener {
            finish()
        }

    }
}