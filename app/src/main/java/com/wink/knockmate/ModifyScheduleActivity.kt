package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ModifyScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.addschedule)

        AddScheduleInfo.reset()
        val calendarId = intent.getStringExtra("calendarId")

        val prefUser = getSharedPreferences("LoginInfo", MODE_PRIVATE)
        val email = prefUser?.getString("email", "email")
        prefUser?.getString(
            "email",
            "email"
        )?.let { Log.v("test", it) }
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .url(
                "http://3.35.146.57:3000/user?query=${email}"
            )
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log1", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun run() {
                        if (response.code() == 200) {
                            val res = JSONObject(response.body()?.string())
                            Log.v("test", res.toString())
                            try {
                                val resTemp = res.getJSONArray("data")
                                AddScheduleInfo.userEmail =
                                    resTemp.getJSONObject(0).getString("email")
                                AddScheduleInfo.userId =
                                    resTemp.getJSONObject(0).getString("id")
                                AddScheduleInfo.color = resTemp.getJSONObject(0).getInt("color")
                            } catch (e: Exception) {
                                finish()
                                Toast
                                    .makeText(
                                        baseContext,
                                        "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        } else {
                            finish()
                            Toast
                                .makeText(
                                    baseContext,
                                    "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }.run()
            }
        })

        val request2: Request = Request.Builder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .url(
                "http://3.35.146.57:3000/calendaruser?id=${calendarId}"
            )
            .get()
            .build()
        client.newCall(request2).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log1", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun run() {
                        if (response.code() == 200) {
                            val res = JSONObject(response.body()?.string())
                            Log.v("test", res.toString())
                            try {
                                val resTemp = res.getJSONArray("data")
                                AddScheduleInfo.userEmail =
                                    resTemp.getJSONObject(0).getString("email")
                                AddScheduleInfo.userId =
                                    resTemp.getJSONObject(0).getString("id")
                                AddScheduleInfo.color = resTemp.getJSONObject(0).getInt("color")
                            } catch (e: Exception) {
                                finish()
                                Toast
                                    .makeText(
                                        baseContext,
                                        "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        } else {
                            finish()
                            Toast
                                .makeText(
                                    baseContext,
                                    "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }.run()
            }
        })
    }
}

