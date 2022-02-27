package com.wink.knockmate

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

        var isClicked1 = false
        var isClicked2 = false
        var isClicked3 = false
        var isClicked4 = false



        char_choice_char1.setOnClickListener{
            if (!isClicked2&&!isClicked3&&!isClicked4){

                if (!isClicked1){
                    char_choice_char1.setBackgroundResource(R.drawable.bt_radius_orange)
                    isClicked1=true
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt_orange)

                }
                else{
                    char_choice_char1.setBackgroundResource(R.drawable.bt_border)
                    isClicked1=false
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt)
                }

            }
        }

        char_choice_char2.setOnClickListener{
            if (!isClicked1&&!isClicked3&&!isClicked4){

                if (!isClicked4){
                    char_choice_char2.setBackgroundResource(R.drawable.bt_radius_orange)
                    isClicked2=true
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt_orange)

                }
                else{
                    char_choice_char2.setBackgroundResource(R.drawable.bt_border)
                    isClicked4=false
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt)
                }

            }
        }


        char_choice_char3.setOnClickListener{
            if (!isClicked1&&!isClicked2&&!isClicked4){

                if (!isClicked3){
                    char_choice_char3.setBackgroundResource(R.drawable.bt_radius_orange)
                    isClicked3=true
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt_orange)

                }
                else{
                    char_choice_char3.setBackgroundResource(R.drawable.bt_border)
                    isClicked3=false
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt)
                }

            }

        }

        char_choice_char4.setOnClickListener{
            if (!isClicked1&&!isClicked2&&!isClicked3){

                if (!isClicked4){
                    char_choice_char4.setBackgroundResource(R.drawable.bt_radius_orange)
                    isClicked4=true
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt_orange)
                }
                else{
                    char_choice_char4.setBackgroundResource(R.drawable.bt_border)
                    isClicked4=false
                    bt_char_choice_footer.setImageResource(R.drawable.ic_char_choice_bt)
                }

            }

        }

        bt_char_choice_footer.setOnClickListener {
            var color = ""
            when(true){
                isClicked1 -> color = "0"
                isClicked2 -> color = "1"
                isClicked3 -> color = "2"
                isClicked4 -> color = "3"
            }
            if(color!=""){
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email","dy@test.com")
                    .add("color", "${color}")
                    .build()
                val request :Request=
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/color").put(body).build()
                Log.d("color","${color}")
            }

        }
    }
}