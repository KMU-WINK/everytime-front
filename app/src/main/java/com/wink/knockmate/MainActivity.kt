package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {
    lateinit var itemAdapter : Addschedule_invite_detail_Follower_Adapter
    var followerList = mutableListOf<UserModel>()
    lateinit var knockmate_recycler : RecyclerView

    lateinit var invitedAdapter: Addschedule_invited_item_Adapter
    var inviteList = mutableListOf<UserModel>()
    lateinit var invited_recycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_invite_detail)

        val okButton = findViewById<TextView>(R.id.invite_ok_button)
        knockmate_recycler = findViewById(R.id.invite_knockmate_recycler)
        invited_recycler = findViewById(R.id.to_invite_users)

//        okButton.setOnClickListener
//            parentFragment?.childFragmentManager
//                ?.beginTransaction()
//                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
//                ?.addToBackStack(null)
//                ?.commit()
//        }

        initFollowerRecycler()
//        itemAdapter.setOnCheckBoxClickListener(object : Addschedule_invite_detail_Follower_Adapter.OnCheckBoxClickListener{
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onCheckClick(v: CheckBox, data: UserModel, pos: Int) {
//                //초대할 명단 추가하기
//                followerList[pos].invite = v.isChecked
//                if(followerList[pos].invite){
//                    AddScheduleInfo.invitersNumber++
//                    inviteList.apply {
//                        inviteList.add(followerList[pos])
//                        invitedAdapter.datas = inviteList
//                        invitedAdapter.notifyDataSetChanged()
//                    }
//                }else{
//                    AddScheduleInfo.invitersNumber--
//                    inviteList.apply {
//                        inviteList.remove(followerList[pos])
//                        invitedAdapter.datas = inviteList
//                        invitedAdapter.notifyDataSetChanged()
//                    }
//
//                }
//            }
//        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initInvitedRecycler(){
        invitedAdapter = Addschedule_invited_item_Adapter(applicationContext)
        invited_recycler.adapter = invitedAdapter
        inviteList.apply {
            for(i in 0 until followerList.size){
                if(followerList[i].invite){
                    inviteList.add(followerList[i])
                }
            }
            //groupList 버전도 추가
            invitedAdapter.datas = inviteList
            invitedAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFollowerRecycler(){
        itemAdapter = Addschedule_invite_detail_Follower_Adapter(this)
        knockmate_recycler.setHasFixedSize(true)
        knockmate_recycler.layoutManager = LinearLayoutManager(this)
        knockmate_recycler.adapter = itemAdapter

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .url("http://3.35.146.57:3000/myfollower?email=dy@test.com")
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
                        if (response.code == 200) {
                            val res = JSONObject(response.body?.string())
                            val resTemp = res.getJSONArray("data")
                            followerList.apply {
                                for(i in 0 until resTemp.length()){
                                    followerList.add(
                                        UserModel(
                                            resTemp.getJSONObject(i).getString("id"),
                                            resTemp.getJSONObject(i).getString("nickname"),
                                            true,
                                            resTemp.getJSONObject(i).getString("email"),
                                        )
                                    )
                                }
                            }
                        } else if (response.code == 201) {
                        } else {
                        }
                    }
                }.run()
            }
        })

        itemAdapter.datas = followerList
        itemAdapter.notifyDataSetChanged()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val btn = findViewById<Button>(R.id.button)
//        btn.setOnClickListener {
//            val client = OkHttpClient().newBuilder()
//                .build()
//            val request: Request = Request.Builder()
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .url("http://3.35.146.57:3000/searchuser?query=t")
//                .get()
//                .build()
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d("log1", e.message.toString())
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    object : Thread() {
//                        override fun run() {
//                            if (response.code == 200) {
//                                val res = JSONObject(response.body?.string())
//                                val resTemp = res.getJSONArray("data")
//                                Log.d("log2", resTemp[0].toString())
//                                Log.d("log2", resTemp.getJSONObject(0).getString("id"))
//                            } else if (response.code == 201) {
//                            } else {
//                            }
//                        }
//                    }.run()
//                }
//            })
//        }
//    }
}