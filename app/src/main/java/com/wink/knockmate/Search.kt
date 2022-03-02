package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class Search : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val toMakeGroup = findViewById<ConstraintLayout>(R.id.make_group)
        val searchEdit = findViewById<EditText>(R.id.search)

        val followerFrame = findViewById<ConstraintLayout>(R.id.followers)
        val followGroupFrame = findViewById<ConstraintLayout>(R.id.followgroup)
        val searchUserFrame = findViewById<ConstraintLayout>(R.id.search_user)
        val searchGroupFrame = findViewById<ConstraintLayout>(R.id.search_group)

        val userSearchRecycler = findViewById<RecyclerView>(R.id.search_user_list)
        val groupSearchRecycler = findViewById<RecyclerView>(R.id.search_group_list)


        val searchUserAdapter = Search_Item_Adapter(this)
        userSearchRecycler.adapter = searchUserAdapter

        val searchGroupAdapter = Search_Item_Adapter(this)
        groupSearchRecycler.adapter = searchGroupAdapter

        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //검색
                val id = searchEdit.text.toString()

                if (id !== "") {
                    followGroupFrame.visibility = View.GONE
                    followerFrame.visibility = View.GONE
                    searchGroupFrame.visibility = View.VISIBLE
                    searchUserFrame.visibility = View.VISIBLE
                } else {
                    followGroupFrame.visibility = View.VISIBLE
                    followerFrame.visibility = View.VISIBLE
                    searchGroupFrame.visibility = View.GONE
                    searchUserFrame.visibility = View.GONE
                    return
                }
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url("http://3.35.146.57:3000/searchuser?query=${id}")
                    .method("GET", null)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "인터넷 연결 불안정")
                    }

                    override fun onResponse(call: Call, response1: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response1.code() == 200) {
                                    searchUserAdapter.datas.clear()
                                    val arr =
                                        JSONObject(response1.body()?.string()).getJSONArray("data")
                                    for (i: Int in 0 until arr.length()) {
                                        searchUserAdapter.datas.add(
                                            UserModel(
                                                arr.getJSONObject(i).getString("id"),
                                                arr.getJSONObject(i).getString("nickname"),
                                                false,
                                                arr.getJSONObject(i).getString("email"),
                                                i,
                                                false,
                                                true,
                                                false,
                                            )
                                        )
                                    }
                                    runOnUiThread {
                                        searchUserAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }.run()
                    }
                })
                val request1: Request = Request.Builder()
                    .url("http://3.35.146.57:3000/searchgroup?query=${id}")
                    .method("GET", null)
                    .build()
                client.newCall(request1).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "인터넷 연결 불안정")
                    }

                    override fun onResponse(call: Call, response1: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response1.code() == 200) {
                                    searchGroupAdapter.datas.clear()
                                    val arr =
                                        JSONObject(response1.body()?.string()).getJSONArray("data")
                                    for (i: Int in 0 until arr.length()) {
                                        searchGroupAdapter.datas.add(
                                            UserModel(
                                                arr.getJSONObject(i).getString("id"),
                                                arr.getJSONObject(i).getString("nickname"),
                                                false,
                                                arr.getJSONObject(i).getString("email"),
                                                i,
                                                false,
                                                true,
                                                false,
                                            )
                                        )
                                    }
                                    runOnUiThread {
                                        searchGroupAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }.run()
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        toMakeGroup.setOnClickListener {
            //그룹만들기로 이동
        }
    }
}