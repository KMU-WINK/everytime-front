package com.wink.knockmate

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class InviteGroupMemActivity : AppCompatActivity() {

    var uri: Uri = Uri.parse("android.resource://com.wink.knockmate/drawable/profile_pic")
    lateinit var item: MutableList<ProfileInviteAdapter.ProfileData>

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ProfileInviteAdapter.CustomViewHolder>? = null
    lateinit var recyclerView: RecyclerView

    companion object {
        lateinit var instace: InviteGroupMemActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_group_mem)
        item = mutableListOf()
        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        var email = pref.getString("email", "").toString()

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("http://3.35.146.57:3000/myfollower?email=${email}")
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
                            item.clear()
                            val arr =
                                JSONObject(response1.body()?.string()).getJSONArray("data")
                            for (i: Int in 0 until arr.length()) {
                                item.add(
                                    ProfileInviteAdapter.ProfileData(
                                        arr.getJSONObject(i).getString("nickname"),
                                        arr.getJSONObject(i).getString("email"),
                                        uri
                                    )
                                )
                            }
                            runOnUiThread {
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }.run()
            }
        })

        instace = this
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recycleInviteUser)
        recyclerView.layoutManager = layoutManager

        adapter = ProfileInviteAdapter(item)
        recyclerView.adapter = adapter

        findViewById<EditText>(R.id.invite_group_edittext).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (adapter as ProfileInviteAdapter)?.filter?.filter(charSequence)
            }

            override fun afterTextChanged(charSequence: Editable?) {
            }
        })

        val chipgroup = findViewById<ChipGroup>(R.id.invite_group_chipgroup)
        (adapter as ProfileInviteAdapter).setOnCheckBoxClickListener(object :
            ProfileInviteAdapter.OnCheckBoxClickListener {
            override fun onCheckBoxClick(pos: Int) {
                chipgroup.removeAllViews()
                for (it in item) {
                    if (it.isChecked) {
                        val chip =
                            layoutInflater.inflate(R.layout.chip_invite_group, chipgroup, false)
                        (chip as Chip).setOnCloseIconClickListener {

                        }
                        chipgroup.addView(chip)
                    }
                }
            }
        })
    }
}