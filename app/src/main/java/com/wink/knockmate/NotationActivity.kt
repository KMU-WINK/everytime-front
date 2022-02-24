package com.wink.knockmate

import android.graphics.Color.rgb
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class NotationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notation)

        val recyclerKnockNotation = findViewById<RecyclerView>(R.id.recyclerKnockNotation)
        val recyclerFollowNotation = findViewById<RecyclerView>(R.id.recyclerFollowNotation)
        val pref = getSharedPreferences(
            "loginInfo",
            AppCompatActivity.MODE_PRIVATE
        )

        val email = pref.getString("email", "").toString()

        val followList = ArrayList<FollowInfoAdapter.FollowList>()

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(
                "http://3.35.146.57:3000/notification_follow?email=${
                    email
                }"
            )
            .method("GET", null)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "인터넷 연결 불안정")
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    override fun run() {
                        when {
                            response.code() == 200 -> {
                                followList.clear()
                                val data = JSONObject(response.body()?.string())
                                val arr = data.getJSONArray("data")

                                for (i: Int in 0 until arr.length()) {
                                    followList.add(
                                        FollowInfoAdapter.FollowList(
                                            arr.getJSONObject(i).getString("nickname"),
                                            arr.getJSONObject(i).getString("email")
                                        )
                                    )
                                }
                                recyclerFollowNotation.adapter?.notifyDataSetChanged()
                            }
                            response.code() == 201 -> {
                            }
                            else -> {
                            }
                        }
                    }
                }.run()
            }
        })

        val knockList = ArrayList<KnockNotationAdapter.KnockList>()

        client.newCall(
            Request.Builder()
                .url(
                    "http://3.35.146.57:3000/notification_knock?email=${
                        email
                    }"
                )
                .method("GET", null)
                .build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "인터넷 연결 불안정")
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    override fun run() {
                        when {
                            response.code() == 200 -> {
                                knockList.clear()
                                val data = JSONObject(response.body()?.string())
                                val arr = data.getJSONArray("data")
                                for (i: Int in 0 until arr.length()) {
                                    val start = Calendar.getInstance()
                                    val end = Calendar.getInstance()
                                    start.set(
                                        Calendar.HOUR,
                                        arr.getJSONObject(i).getString("startDate")
                                            .split(' ')[1].split(':')[0].toInt()
                                    )
                                    start.set(
                                        Calendar.MINUTE,
                                        arr.getJSONObject(i).getString("startDate")
                                            .split(' ')[1].split(':')[1].toInt()
                                    )
                                    start.set(
                                        Calendar.DAY_OF_MONTH,
                                        arr.getJSONObject(i).getString("startDate")
                                            .split(' ')[0].split('-')[2].toInt()
                                    )
                                    start.set(
                                        Calendar.MONTH,
                                        arr.getJSONObject(i).getString("startDate")
                                            .split(' ')[0].split('-')[1].toInt() - 1
                                    )
                                    start.set(
                                        Calendar.YEAR,
                                        arr.getJSONObject(i).getString("startDate")
                                            .split(' ')[0].split('-')[0].toInt()
                                    )
                                    end.set(
                                        Calendar.HOUR,
                                        arr.getJSONObject(i).getString("endDate")
                                            .split(' ')[1].split(':')[0].toInt()
                                    )
                                    end.set(
                                        Calendar.MINUTE,
                                        arr.getJSONObject(i).getString("endDate")
                                            .split(' ')[1].split(':')[1].toInt()
                                    )
                                    end.set(
                                        Calendar.DAY_OF_MONTH,
                                        arr.getJSONObject(i).getString("endDate")
                                            .split(' ')[0].split('-')[2].toInt()
                                    )
                                    end.set(
                                        Calendar.MONTH,
                                        arr.getJSONObject(i).getString("endDate")
                                            .split(' ')[0].split('-')[1].toInt() - 1
                                    )
                                    end.set(
                                        Calendar.YEAR,
                                        arr.getJSONObject(i).getString("endDate")
                                            .split(' ')[0].split('-')[0].toInt()
                                    )

                                    knockList.add(
                                        KnockNotationAdapter.KnockList(
                                            arr.getJSONObject(i).getString("nickname"),
                                            arr.getJSONObject(i).getString("email"),
                                            arr.getJSONObject(i).getInt("calendarid"),
                                            start,
                                            end,
                                            arr.getJSONObject(i).getString("memo")
                                        )
                                    )
                                }
                                recyclerKnockNotation.adapter?.notifyDataSetChanged()
                            }
                            response.code() == 201 -> {
                            }
                            else -> {
                            }
                        }
                    }
                }.run()
            }
        })

        recyclerKnockNotation.layoutManager = LinearLayoutManager(this)
        recyclerKnockNotation.adapter = KnockNotationAdapter(knockList, this)
        recyclerKnockNotation.visibility = View.GONE

        recyclerFollowNotation.layoutManager = LinearLayoutManager(this)
        recyclerFollowNotation.adapter = FollowInfoAdapter(followList, this)

        val btNotationKnock = findViewById<Button>(R.id.btNotationKnock)
        val btNotationFollow = findViewById<Button>(R.id.btNotationFollow)
        val btNotationFollowUnder = findViewById<ImageView>(R.id.btNotationFollowUnder)
        val btNotationKnockUnder = findViewById<ImageView>(R.id.btNotationKnockUnder)

        findViewById<ImageButton>(R.id.btNotationBack).setOnClickListener {
            finish()
        }

        btNotationKnock.setOnClickListener {
            btNotationKnock.setTextColor(rgb(255, 122, 83))
            btNotationFollow.setTextColor(rgb(161, 161, 161))
            btNotationFollowUnder.setBackgroundColor(rgb(255, 255, 255))
            btNotationKnockUnder.setBackgroundColor(rgb(255, 122, 83))
            recyclerFollowNotation.visibility = View.GONE
            recyclerKnockNotation.visibility = View.VISIBLE

        }
        btNotationFollow.setOnClickListener {
            btNotationKnock.setTextColor(rgb(161, 161, 161))
            btNotationFollow.setTextColor(rgb(255, 122, 83))
            btNotationFollowUnder.setBackgroundColor(rgb(255, 122, 83))
            btNotationKnockUnder.setBackgroundColor(rgb(255, 255, 255))
            recyclerFollowNotation.visibility = View.VISIBLE
            recyclerKnockNotation.visibility = View.GONE

        }
    }


}