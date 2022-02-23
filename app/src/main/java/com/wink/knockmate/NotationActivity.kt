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
        val followList = ArrayList<FollowInfoAdapter.FollowList>()

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(
                "http://3.35.146.57:3000/notification_follow?email=${
                    pref.getString("email", "").toString()
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

        val knockList = arrayOf(
            KnockNotationAdapter.KnockList("shinba", 3, 3, 15, 30, 16, 30, "놀자"),
            KnockNotationAdapter.KnockList("shinba", 3, 5, 16, 30, 18, 30, "팀플"),
            KnockNotationAdapter.KnockList("shinba", 3, 10, 16, 30, 18, 30, "팀플"),
            KnockNotationAdapter.KnockList("shinba", 3, 19, 16, 30, 18, 30, "팀플")
        )
        recyclerKnockNotation.layoutManager = LinearLayoutManager(this)
        recyclerKnockNotation.adapter = KnockNotationAdapter(knockList)
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