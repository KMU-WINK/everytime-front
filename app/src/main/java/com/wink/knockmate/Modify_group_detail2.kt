package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Modify_group_detail2 : AppCompatActivity() {
    val userList = mutableListOf<UserModel>()
    lateinit var itemAdapter: Addschedule_invite_detail_groupMem_Adapter
    lateinit var recycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_invite_detail_group)

        val id = intent.getStringExtra("id")

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .url(
                "http://3.35.146.57:3000/user?query=${id}"
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
                            val resTemp = res.getJSONArray("data")
                            for (i in 0 until resTemp.length()) {
                                userList.add(
                                    UserModel(
                                        resTemp.getJSONObject(i).getString("id"),
                                        resTemp.getJSONObject(i).getString("nickname"),
                                        true,
                                        resTemp.getJSONObject(i).getString("email"),
                                        i
                                    )
                                )
                            }
                        } else {
                        }
                    }
                }.run()
            }
        })

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }

        val saveButton = findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }

        recycler = findViewById(R.id.invite_detail_group_member_recycler)

        initMembersRecycler()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initMembersRecycler() {
        itemAdapter = Addschedule_invite_detail_groupMem_Adapter()
        recycler.layoutManager =
            LinearLayoutManager(baseContext)
        recycler.adapter = itemAdapter

        itemAdapter.datas = userList
        itemAdapter.notifyDataSetChanged()
    }
}