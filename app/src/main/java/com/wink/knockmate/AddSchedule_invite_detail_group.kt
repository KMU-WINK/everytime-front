package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AddSchedule_invite_detail_group : Fragment() {
    val userList = mutableListOf<UserModel>()
    lateinit var itemAdapter: Addschedule_invite_detail_groupMem_Adapter
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = arguments?.getString("id")

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.addschedule_invite_detail_group, container, false)

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        val saveButton = view.findViewById<TextView>(R.id.save_button)
        backButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, Addschedule_invite_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        saveButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, Addschedule_invite_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        recycler = view.findViewById(R.id.invite_detail_group_member_recycler)

        initMembersRecycler()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initMembersRecycler() {
        itemAdapter = Addschedule_invite_detail_groupMem_Adapter()
        recycler.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        recycler.adapter = itemAdapter

        itemAdapter.datas = userList
        itemAdapter.notifyDataSetChanged()
    }
}