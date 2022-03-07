package com.wink.knockmate

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.rgb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_character_choice.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class CharacterChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_choice)

        var clickedIdx = -1

        char_choice_back.setOnClickListener {
            finish()
        }

        char_choice_char1.setOnClickListener {
            reset()
            if (clickedIdx != 0) {
                char_choice_char1.setBackgroundResource(R.drawable.bt_radius_orange)
                bt_char_choice_footer.background =
                    resources.getDrawable(R.drawable.ic_char_choice_bt_orange)
                clickedIdx = 0
            }
        }

        char_choice_char2.setOnClickListener {
            reset()
            if (clickedIdx != 1) {
                char_choice_char2.setBackgroundResource(R.drawable.bt_radius_orange)
                bt_char_choice_footer.background =
                    resources.getDrawable(R.drawable.ic_char_choice_bt_orange)
                clickedIdx = 1
            }
        }


        char_choice_char3.setOnClickListener {
            reset()
            if (clickedIdx != 2) {
                char_choice_char3.setBackgroundResource(R.drawable.bt_radius_orange)
                bt_char_choice_footer.background =
                    resources.getDrawable(R.drawable.ic_char_choice_bt_orange)
                clickedIdx = 2
            }
        }

        char_choice_char4.setOnClickListener {
            reset()
            if (clickedIdx != 3) {
                char_choice_char4.setBackgroundResource(R.drawable.bt_radius_orange)
                bt_char_choice_footer.background =
                    resources.getDrawable(R.drawable.ic_char_choice_bt_orange)
                clickedIdx = 3
            }
        }

        bt_char_choice_footer.setOnClickListener {
            if (clickedIdx != -1) {
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", "dy@test.com")
                    .add("color", "${clickedIdx}")
                    .build()
                val request: Request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/color").put(body).build()

                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }
    }

    fun reset() {
        char_choice_char1.setBackgroundResource(R.drawable.bt_border)
        char_choice_char2.setBackgroundResource(R.drawable.bt_border)
        char_choice_char3.setBackgroundResource(R.drawable.bt_border)
        char_choice_char4.setBackgroundResource(R.drawable.bt_border)
        bt_char_choice_footer.background =
            resources.getDrawable(R.drawable.ic_char_choice_bt)
    }
}