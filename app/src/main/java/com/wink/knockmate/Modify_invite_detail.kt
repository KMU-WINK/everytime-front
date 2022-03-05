package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Modify_invite_detail : AppCompatActivity() {
    lateinit var itemAdapter: Addschedule_invite_detail_Follower_Adapter
    lateinit var knockmate_recycler: RecyclerView

    lateinit var groupAdapter: Addschedule_invite_detail_Follower_Adapter
    lateinit var group_recycler: RecyclerView

    lateinit var invitedAdapter: Addschedule_invited_item_Adapter
    var inviteList = mutableListOf<UserModel>()
    lateinit var invited_recycler: RecyclerView

    lateinit var invitedGrayAdapter: Addschedule_invited_gray_item_Adapter
    var inviteGrayList = mutableListOf<UserModel>()
    lateinit var invited_gray_recycler: RecyclerView

    var tempInvitedList = mutableListOf<UserModel>()
    private var tempInvitedNumber = 0
    private var tempInvitedList2 = mutableListOf<UserModel>()

    var invitedBoolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.addschedule_invite_detail)

        for (i in 0 until AddScheduleInfo.inviteMembers.size) {
            tempInvitedList.add(AddScheduleInfo.inviteMembers[i].copy())
        }
        Log.v("test6", tempInvitedList.toString())

        val okButton = findViewById<TextView>(R.id.invite_ok_button)
        val backButton = findViewById<View>(R.id.back_button)
        knockmate_recycler = findViewById(R.id.invite_knockmate_recycler)
        invited_recycler = findViewById(R.id.to_invite_users)
        group_recycler = findViewById(R.id.invite_group_member_recycler)
        invited_gray_recycler = findViewById(R.id.to_invited_users)

        tempInvitedList = AddScheduleInfo.inviteMembers
        tempInvitedNumber = AddScheduleInfo.invitersNumber

        okButton.setOnClickListener {
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener {
            if (tempInvitedNumber == 0) {
                AddScheduleInfo.inviteMembers = mutableListOf()
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    AddScheduleInfo.followerList[i].invite = false
                }
            } else {
                AddScheduleInfo.inviteMembers = mutableListOf()
                val tempNumbers = mutableListOf(-1)
                for (i in 0 until tempInvitedList.size) {
                    tempInvitedList[i].sequence?.let { it1 ->
                        tempNumbers.apply {
                            tempNumbers.add(it1)
                        }
                    }
                }
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    if (AddScheduleInfo.followerList[i].sequence !in tempNumbers) {
                        AddScheduleInfo.followerList[i].invite = false
                    }
                }
                for (i in 0 until tempInvitedList.size) {
                    AddScheduleInfo.inviteMembers.add(tempInvitedList[i])
                }
//                for (i in 0 until AddScheduleInfo.inviteMembers.size) {
//                    if (AddScheduleInfo.inviteMembers[i].sequence !in tempNumbers) {
//                        AddScheduleInfo.inviteMembers[i].invite = false
//                        AddScheduleInfo.inviteMembers.remove(AddScheduleInfo.inviteMembers[i])
//                    }
//                }
            }
            Log.v("test5", AddScheduleInfo.inviteMembers.toString())
            AddScheduleInfo.invitersNumber = tempInvitedNumber
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        }

        Log.v("followers", AddScheduleInfo.followerList.toString())
        Log.v("test4", AddScheduleInfo.inviteMembers.toString())
        Log.v("test2", tempInvitedNumber.toString())
        Log.v("test3", tempInvitedList.toString())

        initInvitedRecycler()
        initFollowerRecycler()
        initGroupRecycler()
        initInvitedGrayRecycler()

        itemAdapter.setOnCheckBoxClickListener(object :
            Addschedule_invite_detail_Follower_Adapter.OnCheckBoxClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onCheckClick(v: CheckBox, data: UserModel, pos: Int) {
                //초대할 명단 추가하기
                if (v.isChecked) {
                    AddScheduleInfo.followerList[pos].invite = v.isChecked
                    AddScheduleInfo.invitersNumber++
                    AddScheduleInfo.inviteMembers.add(AddScheduleInfo.followerList[pos].copy())
                    inviteList.apply {
                        inviteList.add(AddScheduleInfo.followerList[pos])
                    }
                    if (inviteList.size >= 1) {
                        invitedBoolean = true
                    }
                    Log.v("test7", tempInvitedList.toString())
                } else {
                    AddScheduleInfo.invitersNumber--
                    inviteList.apply {
                        AddScheduleInfo.inviteMembers.remove(AddScheduleInfo.followerList[pos].copy())
                        inviteList.remove(AddScheduleInfo.followerList[pos])
                    }
                    if (inviteList.size == 0) {
                        invitedBoolean = false
                    }
                    AddScheduleInfo.followerList[pos].invite = v.isChecked
                }
                if (invitedBoolean) {
                    invited_recycler.visibility = View.VISIBLE
                } else {
                    invited_recycler.visibility = View.GONE
                }
                invitedAdapter.datas = inviteList
                invitedAdapter.notifyDataSetChanged()
            }
        })

        groupAdapter.setOnCheckBoxClickListener(object :
            Addschedule_invite_detail_Follower_Adapter.OnCheckBoxClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onCheckClick(v: CheckBox, data: UserModel, pos: Int) {
                if (v.isChecked) {
                    AddScheduleInfo.groupList[pos].invite = v.isChecked
                    AddScheduleInfo.inviteGroupsNumber++
                    AddScheduleInfo.inviteGroups.add(AddScheduleInfo.groupList[pos].copy())
                    inviteList.apply {
                        inviteList.add(AddScheduleInfo.groupList[pos])
                    }
                    if (inviteList.size >= 1) {
                        invitedBoolean = true
                    }
                    Log.v("test7", tempInvitedList.toString())
                } else {
                    AddScheduleInfo.inviteGroupsNumber--
                    inviteList.apply {
                        AddScheduleInfo.inviteGroups.remove(AddScheduleInfo.groupList[pos].copy())
                        inviteList.remove(AddScheduleInfo.groupList[pos])
                    }
                    if (inviteList.size == 0) {
                        invitedBoolean = false
                    }
                    AddScheduleInfo.groupList[pos].invite = v.isChecked
                }
                if (invitedBoolean) {
                    invited_recycler.visibility = View.VISIBLE
                } else {
                    invited_recycler.visibility = View.GONE
                }
                invitedAdapter.datas = inviteList
                invitedAdapter.notifyDataSetChanged()
            }
        })

        invitedAdapter.setOnDeleteButtonClickListener(object :
            Addschedule_invited_item_Adapter.OnDeleteButtonClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDeleteButtonClick(v: TextView, data: UserModel, pos: Int) {
                AddScheduleInfo.followerList[data.sequence!!].invite = false
                itemAdapter.datas = AddScheduleInfo.followerList
                itemAdapter.notifyDataSetChanged()
                AddScheduleInfo.inviteMembers.remove(data)
                inviteList.remove(data)
                invitedAdapter.datas = inviteList
                invitedAdapter.notifyDataSetChanged()
                invitedBoolean = false
                if (!invitedBoolean) {
                    invited_recycler.visibility = View.GONE
                }
            }

        })

        groupAdapter.setOnArrowClickListener(object :
            Addschedule_invite_detail_Follower_Adapter.OnArrowClickListener {
            override fun onArrowClick(v: ImageView, data: UserModel, pos: Int) {

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupRecycler() {
        groupAdapter =
            Addschedule_invite_detail_Follower_Adapter()
        group_recycler.layoutManager =
            LinearLayoutManager(applicationContext)
        group_recycler.adapter = groupAdapter
        groupAdapter.datas = AddScheduleInfo.groupList
        groupAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initInvitedRecycler() {
        invitedAdapter =
            Addschedule_invited_item_Adapter(applicationContext)
        invited_recycler.adapter = invitedAdapter
        if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 0) {
            invited_recycler.visibility = View.GONE
        } else {
            invited_recycler.visibility = View.VISIBLE
        }
        inviteList.apply {
            for (i in 0 until AddScheduleInfo.inviteMembers.size) {
                inviteList.add(AddScheduleInfo.inviteMembers[i])
            }
            for (i in 0 until AddScheduleInfo.inviteGroups.size) {
                inviteList.add(AddScheduleInfo.inviteGroups[i])
            }
            invitedAdapter.datas = inviteList
            invitedAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initInvitedGrayRecycler() {
        invitedGrayAdapter = Addschedule_invited_gray_item_Adapter(applicationContext)
        invited_gray_recycler.adapter = invitedGrayAdapter
        if (AddScheduleInfo.invitedMembers.size == 0) {
            invited_gray_recycler.visibility = View.GONE
        } else {
            invited_gray_recycler.visibility = View.VISIBLE
        }
        invitedGrayAdapter.datas = inviteGrayList
        invitedGrayAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFollowerRecycler() {
        itemAdapter =
            Addschedule_invite_detail_Follower_Adapter()
        knockmate_recycler.layoutManager =
            LinearLayoutManager(applicationContext)
        knockmate_recycler.adapter = itemAdapter

        itemAdapter.datas = AddScheduleInfo.followerList
        itemAdapter.notifyDataSetChanged()
    }
}