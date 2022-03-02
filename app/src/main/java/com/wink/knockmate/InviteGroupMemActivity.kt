package com.wink.knockmate

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_invite_group_mem.*
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

    lateinit var nickname: String
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_group_mem)
        nickname = intent.extras?.getString("nickname").toString()
        item = mutableListOf()
        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        email = pref.getString("email", "").toString()

        invite_group_back.setOnClickListener {
            finish()
        }

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
                                        uri,
                                        false
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

        (adapter as ProfileInviteAdapter).setOnCheckBoxClickListener(object :
            ProfileInviteAdapter.OnCheckBoxClickListener {
            override fun onCheckBoxClick(pos: Int) {
                refreshChipGroup()
            }
        })
        refreshChipGroup()
    }

    fun refreshChipGroup() {
        val chipgroup = findViewById<ChipGroup>(R.id.invite_group_chipgroup)
        chipgroup.removeAllViews()
        var i = 0
        var createable = false
        for (it in item) {
            var t = i
            i += 1
            if (it.isChecked) {
                createable = true
                val chip =
                    layoutInflater.inflate(R.layout.chip_invite_group, chipgroup, false)
                (chip as Chip).text = it.ProName
                (chip as Chip).setOnCloseIconClickListener {
                    item[t] = ProfileInviteAdapter.ProfileData(
                        item[t].ProName,
                        item[t].ProEmail,
                        uri,
                        false
                    )
                    runOnUiThread {
                        refreshChipGroup()
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
                chipgroup.addView(chip)
            }
        }
        if (createable) {
            btInviteMem.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.brand_main))
            btInviteMem.setOnClickListener {
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("nickname", nickname)
                    .add("userid", email.split('@')[0])
                for (it in item) {
                    if (it.isChecked)
                        body.add("userid", it.ProEmail.split('@')[0])
                }

                val request: Request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/group").post(body.build()).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response.code() == 200) {
                                    val intt = Intent(
                                        this@InviteGroupMemActivity,
                                        KnockmateActivity::class.java
                                    )
                                    intt.putExtra(
                                        "groupid",
                                        nickname
                                    )
                                    startActivity(intt)
                                } else {
                                    Log.d("log", response.message())
                                }
                            }
                        }.run()
                    }
                })
            }
        } else {
            btInviteMem.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.disabled))
            btInviteMem.setOnClickListener {

            }
        }
    }
}