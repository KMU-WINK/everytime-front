package com.wink.knockmate

import android.net.LinkAddress
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InviteGroupMemActivity : AppCompatActivity() {

    var uri :Uri=Uri.parse("android.resource://com.wink.knockmate/drawable/profile_pic")
    private val item = mutableListOf<ProfileInviteAdapter.ProfileData>(ProfileInviteAdapter.ProfileData("신재혁",uri))

    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter : RecyclerView.Adapter<ProfileInviteAdapter.CustomViewHolder>?=null
    lateinit var recyclerView: RecyclerView
    companion object{
        lateinit var instace : InviteGroupMemActivity
    }




    override fun onCreate(savedInstanceState: Bundle?) {

        item.apply {
            add(ProfileInviteAdapter.ProfileData("윤상원",uri))
            add(ProfileInviteAdapter.ProfileData("김도엽",uri))
            add(ProfileInviteAdapter.ProfileData("유은솔",uri))
            add(ProfileInviteAdapter.ProfileData("홍현지",uri))
            add(ProfileInviteAdapter.ProfileData("정나영",uri))

        }






        super.onCreate(savedInstanceState)
        Log.d("item",item.toString())
        setContentView(R.layout.activity_invite_group_mem)

        instace=this
        layoutManager=LinearLayoutManager(this)
        recyclerView=findViewById(R.id.recycleInviteUser)
        recyclerView.layoutManager =layoutManager

        adapter = ProfileInviteAdapter(item)
        recyclerView.adapter=adapter


    }
}