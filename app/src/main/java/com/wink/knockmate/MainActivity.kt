package com.wink.knockmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/searchuser?query=t")
                .get()
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response.code == 200) {
                                val res = JSONObject(response.body?.string())
                                val resTemp = res.getJSONArray("data")
                                Log.d("log2", resTemp[0].toString())
                                Log.d("log2", resTemp.getJSONObject(0).getString("id"))
                            } else if (response.code == 201) {
                            } else {
                            }
                        }
                    }.run()
                }
            })
        }
    }
}