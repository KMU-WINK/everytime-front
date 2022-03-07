package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Modify_invite : AppCompatActivity() {
    lateinit var groupAdapter: Modify_invited_item_Adapter
    var groupInviteList = mutableListOf<UserModel>()
    lateinit var groupInviteView: RecyclerView
    lateinit var userAdapter: Modify_invited_item_Adapter
    var userInviteList = mutableListOf<UserModel>()
    lateinit var userInviteView: RecyclerView
    lateinit var groupFrame: LinearLayout
    lateinit var userFrame: LinearLayout
    var memberNumber = 0
    var groupNumber = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_invite)

        val okButton = findViewById<TextView>(R.id.invite_ok_button)
        val backButton = findViewById<View>(R.id.back_button)
        val inviteTitle = findViewById<TextView>(R.id.invite_title)
        val inviteButton = findViewById<LinearLayout>(R.id.invite_button)
        groupInviteView = findViewById(R.id.group_recycler)
        userInviteView = findViewById(R.id.knockmates_recycler)
        groupFrame = findViewById(R.id.groups_frame)
        userFrame = findViewById(R.id.knockmates_frame)

        var inviterTemp = AddScheduleInfo.invitersNumber

        for (i in 0 until AddScheduleInfo.inviteGroups.size) {
            inviterTemp += AddScheduleInfo.inviteGroups[i].isFav
            AddScheduleInfo.allGroupMembersNumber += AddScheduleInfo.inviteGroups[i].isFav
        }

        inviteTitle.text = "초대 " + inviterTemp.toString() + "명"

        okButton.setOnClickListener {
            AddScheduleInfo.priorInviteMembers = mutableListOf()
            AddScheduleInfo.priorInviteGroups = mutableListOf()
            AddScheduleInfo.priorInviteMembers.addAll(AddScheduleInfo.inviteMembers)
            AddScheduleInfo.priorInvitersNumber = AddScheduleInfo.invitersNumber
            AddScheduleInfo.priorInviteGroupsNumber = AddScheduleInfo.inviteGroupsNumber
            AddScheduleInfo.priorInviteGroups.addAll(AddScheduleInfo.inviteGroups)
            val intent = Intent(this, ModifyScheduleActivity::class.java)
            intent.putExtra("type", "")
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener {
            val tempNumbers = mutableListOf(-1)
            val tempNumbers2 = mutableListOf(-1)
            for (i in 0 until AddScheduleInfo.priorInviteMembers.size) {
                AddScheduleInfo.priorInviteMembers[i].sequence?.let { it1 ->
                    tempNumbers.apply {
                        tempNumbers.add(it1)
                    }
                }
            }
            for (i in 0 until AddScheduleInfo.priorInviteGroups.size) {
                AddScheduleInfo.priorInviteGroups[i].sequence?.let { it1 ->
                    tempNumbers2.apply {
                        tempNumbers2.add(it1)
                    }
                }
            }
            for (i in 0 until AddScheduleInfo.followerList.size) {
                if (AddScheduleInfo.followerList[i].sequence !in tempNumbers) {
                    AddScheduleInfo.followerList[i].invite = false
                }
            }
            for (i in 0 until AddScheduleInfo.groupList.size) {
                if (AddScheduleInfo.groupList[i].sequence !in tempNumbers) {
                    AddScheduleInfo.groupList[i].invite = false
                }
            }
            AddScheduleInfo.inviteGroups = mutableListOf()
            AddScheduleInfo.inviteMembers = mutableListOf()
            AddScheduleInfo.invitersNumber = AddScheduleInfo.priorInvitersNumber
            AddScheduleInfo.inviteMembers.addAll(AddScheduleInfo.priorInviteMembers)
            AddScheduleInfo.inviteGroups.addAll(AddScheduleInfo.priorInviteGroups)
            AddScheduleInfo.inviteGroupsNumber = AddScheduleInfo.priorInviteGroupsNumber
            val intent = Intent(this, ModifyScheduleActivity::class.java)
            intent.putExtra("type", "")
            startActivity(intent)
            finish()
        }

        inviteButton.setOnClickListener {
            val intent = Intent(this, Modify_invite_detail::class.java)
            intent.putExtra("type", "")
            startActivity(intent)
            finish()
        }

        initGroupInviteList()

        groupAdapter.setOnArrowClickListener(object :
            Modify_invited_item_Adapter.OnArrowClickListener {
            override fun onArrowClick(v: ImageView, data: UserModel, pos: Int) {
                val intent = Intent(this@Modify_invite, Modify_group_detail1::class.java)
                startActivity(intent)
                finish()
            }
        })

        userAdapter.setOnMoreClickListener(object :
            Modify_invited_item_Adapter.OnMoreClickListener {
            override fun onMoreClick(v: ImageView, data: UserModel, pos: Int) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupInviteList() {
        groupAdapter =
            Modify_invited_item_Adapter()
        userAdapter =
            Modify_invited_item_Adapter()
        groupInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        userInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        groupInviteView.adapter = groupAdapter
        userInviteView.adapter = userAdapter

        for (i in 0 until AddScheduleInfo.invitedMembers.size) {
            if (AddScheduleInfo.invitedMembers[i].user) {
                userInviteList.add(AddScheduleInfo.invitedMembers[i])
                memberNumber++
            } else {
                groupInviteList.add(AddScheduleInfo.invitedMembers[i])
                groupNumber++
            }
        }

        if (memberNumber >= 1) {
            userFrame.visibility = View.VISIBLE
        }

        if (groupNumber >= 1) {
            groupFrame.visibility = View.VISIBLE
        }

        userAdapter.datas = userInviteList
        userAdapter.notifyDataSetChanged()

        groupAdapter.datas = groupInviteList
        groupAdapter.notifyDataSetChanged()

//        userAdapter.datas = userInviteList
//        userAdapter.notifyDataSetChanged()
//        groupAdapter.datas = groupInviteList
//        groupAdapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val tempNumbers = mutableListOf(-1)
        val tempNumbers2 = mutableListOf(-1)
        for (i in 0 until AddScheduleInfo.priorInviteMembers.size) {
            AddScheduleInfo.priorInviteMembers[i].sequence?.let { it1 ->
                tempNumbers.apply {
                    tempNumbers.add(it1)
                }
            }
        }
        for (i in 0 until AddScheduleInfo.priorInviteGroups.size) {
            AddScheduleInfo.priorInviteGroups[i].sequence?.let { it1 ->
                tempNumbers2.apply {
                    tempNumbers2.add(it1)
                }
            }
        }
        for (i in 0 until AddScheduleInfo.followerList.size) {
            if (AddScheduleInfo.followerList[i].sequence !in tempNumbers) {
                AddScheduleInfo.followerList[i].invite = false
            }
        }
        for (i in 0 until AddScheduleInfo.groupList.size) {
            if (AddScheduleInfo.groupList[i].sequence !in tempNumbers) {
                AddScheduleInfo.groupList[i].invite = false
            }
        }
        AddScheduleInfo.invitersNumber = AddScheduleInfo.priorInvitersNumber
        AddScheduleInfo.inviteMembers = AddScheduleInfo.priorInviteMembers
        AddScheduleInfo.inviteGroups = AddScheduleInfo.priorInviteGroups
        AddScheduleInfo.inviteGroupsNumber = AddScheduleInfo.priorInviteGroupsNumber
        val intent = Intent(this, ModifyScheduleActivity::class.java)
        intent.putExtra("type", "")
        startActivity(intent)
        finish()
    }
}