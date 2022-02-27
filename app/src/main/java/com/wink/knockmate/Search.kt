package com.wink.knockmate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class Search : AppCompatActivity() {
    var email = ""
    lateinit var searchEdit: EditText
    lateinit var followerFrame: ConstraintLayout
    lateinit var followGroupFrame: ConstraintLayout
    lateinit var searchUserFrame: ConstraintLayout
    lateinit var searchGroupFrame: ConstraintLayout

    lateinit var userSearchRecycler: RecyclerView
    lateinit var groupSearchRecycler: RecyclerView
    lateinit var userFollowerRecycler: RecyclerView
    lateinit var groupFollowerRecycler: RecyclerView

    lateinit var searchUserAdapter: Search_Item_Adapter
    lateinit var searchGroupAdapter: Search_Item_Adapter
    lateinit var followerUserAdapter: Search_Follower_Adapter
    lateinit var followerGroupAdapter: Search_FollowGroup_Adapter


    lateinit var toMakeGroup: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        email = pref.getString("email", "").toString()

        searchEdit = findViewById<EditText>(R.id.search)
        followerFrame = findViewById<ConstraintLayout>(R.id.followers)
        followGroupFrame = findViewById<ConstraintLayout>(R.id.followgroup)
        searchUserFrame = findViewById<ConstraintLayout>(R.id.search_user)
        searchGroupFrame = findViewById<ConstraintLayout>(R.id.search_group)

        userSearchRecycler = findViewById<RecyclerView>(R.id.search_user_list)
        groupSearchRecycler = findViewById<RecyclerView>(R.id.search_group_list)
        userFollowerRecycler = findViewById<RecyclerView>(R.id.user_list)
        groupFollowerRecycler = findViewById<RecyclerView>(R.id.group_list)

        searchUserAdapter = Search_Item_Adapter(this)
        searchGroupAdapter = Search_Item_Adapter(this)
        followerUserAdapter = Search_Follower_Adapter(this)
        followerGroupAdapter = Search_FollowGroup_Adapter(this)


        toMakeGroup = findViewById<ConstraintLayout>(R.id.make_group)

        userSearchRecycler.adapter = searchUserAdapter
        groupSearchRecycler.adapter = searchGroupAdapter
        userFollowerRecycler.adapter = followerUserAdapter
        groupFollowerRecycler.adapter = followerGroupAdapter

        refreshLayout()
        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshLayout()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        toMakeGroup.setOnClickListener {
            //그룹만들기로 이동
        }
    }

    fun refreshLayout() {

        val searchMenu = findViewById<ConstraintLayout>(R.id.search_menu)
        val menu_follow = findViewById<TextView>(R.id.search_menu_follow)
        val menu_fav = findViewById<TextView>(R.id.search_menu_fav)
        searchMenu.setOnClickListener {
            searchMenu.visibility = View.GONE
        }
        followerUserAdapter.setOnDetailClickListener(object :
            Search_Follower_Adapter.OnDetailClickListener {
            override fun onDetailClick(v: ImageView, data: UserModel, pos: Int) {
                searchMenu.visibility = View.VISIBLE
                menu_follow.text = "언팔로우"
                menu_fav.text = if (data.isFav != 0) "즐겨찾기 취소" else "즐겨찾기"

                menu_follow.setOnClickListener {
                    val client = OkHttpClient()
                    val body = FormBody.Builder()
                        .add("email", email)
                        .add("targetEmail", data.email)
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/unfollowuser").post(body).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                        runOnUiThread {
                                            searchMenu.visibility = View.GONE
                                            refreshLayout()
                                        }
                                    } else {
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                }
                menu_fav.setOnClickListener {
                    val client = OkHttpClient()
                    val body = FormBody.Builder()
                        .add("email", email)
                        .add("targetEmail", data.email)
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/togglefavuser").post(body).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                        runOnUiThread {
                                            searchMenu.visibility = View.GONE
                                            refreshLayout()
                                        }
                                    } else {
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                }
            }
        })
        followerGroupAdapter.setOnDetailClickListener(object :
            Search_FollowGroup_Adapter.OnDetailClickListener {
            override fun onDetailClick(v: ImageView, data: UserModel, pos: Int) {
                searchMenu.visibility = View.VISIBLE
                menu_follow.text = "그룹 탈퇴"
                menu_fav.text = if (data.isFav != 0) "즐겨찾기 취소" else "즐겨찾기"
                menu_follow.setOnClickListener {
                    val client = OkHttpClient()
                    val body = FormBody.Builder()
                        .add("email", email)
                        .add("groupid", data.id)
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/unfollowgroup").post(body).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                        runOnUiThread {
                                            searchMenu.visibility = View.GONE
                                            refreshLayout()
                                        }
                                    } else {
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                }
                menu_fav.setOnClickListener {
                    val client = OkHttpClient()
                    val body = FormBody.Builder()
                        .add("email", email)
                        .add("groupid", data.id)
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/togglefavgroup").post(body).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                        runOnUiThread {
                                            searchMenu.visibility = View.GONE
                                            refreshLayout()
                                        }
                                    } else {
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                }
            }
        })
        searchUserAdapter.setOnCheckBoxClickListener(object :
            Search_Item_Adapter.OnCheckBoxClickListener {
            override fun onCheckBoxClick(v: ImageButton, data: UserModel, pos: Int) {
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("targetEmail", data.email)
                    .build()
                val request: Request =
                    Request.Builder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/${if (data.follow > 0) "un" else ""}followuser")
                        .post(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "인터넷 연결 불안정")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response.code() == 200) {
                                    runOnUiThread {
                                        refreshLayout()
                                    }
                                } else {
                                    Log.d("log", response.message())
                                }
                            }
                        }.run()
                    }
                })
            }
        })
        searchGroupAdapter.setOnCheckBoxClickListener(object :
            Search_Item_Adapter.OnCheckBoxClickListener {
            override fun onCheckBoxClick(v: ImageButton, data: UserModel, pos: Int) {
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("groupid", data.id)
                    .build()
                val request: Request =
                    Request.Builder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/${if (data.follow > 0) "un" else ""}followgroup")
                        .post(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "인터넷 연결 불안정")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response.code() == 200) {
                                    runOnUiThread {
                                        refreshLayout()
                                    }
                                } else {
                                    Log.d("log", response.message())
                                }
                            }
                        }.run()
                    }
                })
            }
        })


        //검색
        val id = searchEdit.text.toString()

        if (id.isNotEmpty()) {
            toMakeGroup.visibility = View.GONE
            followGroupFrame.visibility = View.GONE
            followerFrame.visibility = View.GONE
            searchGroupFrame.visibility = View.VISIBLE
            searchUserFrame.visibility = View.VISIBLE

            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("http://3.35.146.57:3000/searchuser?query=${id}&email=${email}")
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
                                            false,
                                            true,
                                            arr.getJSONObject(i).getInt("follow"),
                                        )
                                    )
                                }
                                runOnUiThread {
                                    if (arr.length() == 0) {
                                        runOnUiThread {
                                            searchUserFrame.visibility = View.GONE
                                            refreshLayout()
                                        }
                                    }
                                    searchUserAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
            val request1: Request = Request.Builder()
                .url("http://3.35.146.57:3000/searchgroup?query=${id}&email=${email}")
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
                                            "",
                                            false,
                                            true,
                                            arr.getJSONObject(i).getInt("follow"),
                                        )
                                    )
                                }
                                runOnUiThread {
                                    if (arr.length() == 0) {
                                        runOnUiThread {
                                            refreshLayout()
                                            searchGroupFrame.visibility = View.GONE
                                        }
                                    }
                                    searchGroupAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
        } else {
            toMakeGroup.visibility = View.VISIBLE
            followGroupFrame.visibility = View.VISIBLE
            followerFrame.visibility = View.VISIBLE
            searchGroupFrame.visibility = View.GONE
            searchUserFrame.visibility = View.GONE

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
                                followerUserAdapter.datas.clear()
                                val arr =
                                    JSONObject(response1.body()?.string()).getJSONArray("data")
                                for (i: Int in 0 until arr.length()) {
                                    followerUserAdapter.datas.add(
                                        UserModel(
                                            arr.getJSONObject(i).getString("id"),
                                            arr.getJSONObject(i).getString("nickname"),
                                            false,
                                            arr.getJSONObject(i).getString("email"),
                                            false,
                                            true,
                                            1,
                                            arr.getJSONObject(i).getInt("isFav")
                                        )
                                    )
                                }
                                runOnUiThread {
                                    if (arr.length() == 0) {
                                        runOnUiThread {
                                            refreshLayout()
                                            followerFrame.visibility = View.GONE
                                        }
                                    }
                                    followerUserAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
            val request1: Request = Request.Builder()
                .url("http://3.35.146.57:3000/mygroup?email=${email}")
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
                                followerGroupAdapter.datas.clear()
                                val arr =
                                    JSONObject(response1.body()?.string()).getJSONArray("data")
                                for (i: Int in 0 until arr.length()) {
                                    followerGroupAdapter.datas.add(
                                        UserModel(
                                            arr.getJSONObject(i).getString("id"),
                                            arr.getJSONObject(i).getString("nickname"),
                                            false,
                                            "",
                                            false,
                                            true,
                                            1,
                                            arr.getJSONObject(i).getInt("isFav")
                                        )
                                    )
                                }
                                runOnUiThread {
                                    if (arr.length() == 0) {
                                        runOnUiThread {
                                            refreshLayout()
                                            followGroupFrame.visibility = View.GONE
                                        }
                                    }
                                    followerGroupAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
        }

    }
}