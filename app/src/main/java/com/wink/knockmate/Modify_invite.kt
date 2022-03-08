package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Modify_invite : AppCompatActivity() {
    lateinit var invitedGroupAdapter: Modify_invited_item_Adapter
    var invitedGroupInviteList = mutableListOf<UserModel>()
    lateinit var invitedGroupInviteView: RecyclerView
    lateinit var invitedUserAdapter: Modify_invited_item_Adapter
    var invitedUserInviteList = mutableListOf<UserModel>()
    lateinit var invitedUserInviteView: RecyclerView

    lateinit var groupAdapter: Addschedule_invite_item_Adapter
    var groupInviteList = mutableListOf<UserModel>()
    lateinit var groupInviteView: RecyclerView
    lateinit var userAdapter: Addschedule_invite_item_Adapter
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
        invitedUserInviteView = findViewById(R.id.invited_knockmates_recycler)
        invitedGroupInviteView = findViewById(R.id.invited_group_recycler)
        groupInviteView = findViewById(R.id.group_recycler)
        userInviteView = findViewById(R.id.knockmates_recycler)
        groupFrame = findViewById(R.id.groups_frame)
        userFrame = findViewById(R.id.knockmates_frame)

        var inviterTemp = AddScheduleInfo.invitersNumber

        for (i in 0 until AddScheduleInfo.inviteGroups.size) {
            inviterTemp += AddScheduleInfo.inviteGroups[i].isFav
            AddScheduleInfo.allGroupMembersNumber += AddScheduleInfo.inviteGroups[i].isFav
        }

        inviteTitle.text =
            "초대 " + (AddScheduleInfo.invitersNumber + AddScheduleInfo.allGroupMembersNumber).toString() + "명"

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

        invitedGroupAdapter.setOnArrowClickListener(object :
            Modify_invited_item_Adapter.OnArrowClickListener {
            override fun onArrowClick(v: ImageView, data: UserModel, pos: Int) {
                val intent = Intent(this@Modify_invite, Modify_group_detail1::class.java)
                startActivity(intent)
                finish()
            }
        })

        invitedUserAdapter.setOnMoreClickListener(object :
            Modify_invited_item_Adapter.OnMoreClickListener {
            override fun onMoreClick(v: ImageView, data: UserModel, pos: Int) {
                val bottomSheetDialog: Modify_bottomSheetDialog = Modify_bottomSheetDialog {
                    when (it) {
                        0 -> {
                            val bundle = Bundle()
                            if (data.nickname != null) {
                                bundle.putString("name", data.id)
                            } else {
                                bundle.putString("name", data.nickname)
                            }
                            val dialog = Modify_invite_dialog()
                            dialog.arguments = bundle
                            dialog.setButtonClickListener(object :
                                Modify_invite_dialog.OnButtonClickListener {
                                override fun onButton1Clicked() {
                                }

                                override fun onButton2Clicked() {
                                    //노크알람삭제
                                    AddScheduleInfo.invitedMembers.remove(data)
                                    if (data.user) {
                                        userInviteList.remove(data)
                                        AddScheduleInfo.invitersNumber--
                                        InviteData.userNum--
                                    } else {
                                        groupInviteList.remove(data)
                                        AddScheduleInfo.inviteGroupsNumber--
                                        AddScheduleInfo.allGroupMembersNumber -= data.members
                                        InviteData.groupNum--
                                        InviteData.allGroupNum -= data.members
                                    }
                                    onRestart()
                                }
                            })
                            dialog.show(supportFragmentManager, dialog.tag)
                        }
                        1 -> {
                            // 재노크
                        }
                    }
                }
                bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupInviteList() {
        invitedGroupAdapter =
            Modify_invited_item_Adapter()
        invitedUserAdapter =
            Modify_invited_item_Adapter()
        invitedGroupInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        invitedUserInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        invitedGroupInviteView.adapter = invitedGroupAdapter
        invitedUserInviteView.adapter = invitedUserAdapter

        groupAdapter =
            Addschedule_invite_item_Adapter()
        userAdapter =
            Addschedule_invite_item_Adapter()
        groupInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        userInviteView.layoutManager =
            LinearLayoutManager(applicationContext)
        groupInviteView.adapter = groupAdapter
        userInviteView.adapter = userAdapter

        for (i in 0 until AddScheduleInfo.invitedMembers.size) {
            if (AddScheduleInfo.invitedMembers[i].user) {
                invitedUserInviteList.add(AddScheduleInfo.invitedMembers[i])
                memberNumber++
            } else {
                invitedGroupInviteList.add(AddScheduleInfo.invitedMembers[i])
                groupNumber++
            }
        }
        for (i in 0 until AddScheduleInfo.inviteMembers.size) {
            userInviteList.add(AddScheduleInfo.inviteMembers[i])
            memberNumber++
        }
        for (i in 0 until AddScheduleInfo.inviteGroups.size) {
            groupInviteList.add(AddScheduleInfo.inviteGroups[i])
            groupNumber++
        }

        if (memberNumber >= 1) {
            userFrame.visibility = View.VISIBLE
        }

        if (groupNumber >= 1) {
            groupFrame.visibility = View.VISIBLE
        }

        invitedUserAdapter.datas = invitedUserInviteList
        invitedUserAdapter.notifyDataSetChanged()

        invitedGroupAdapter.datas = invitedGroupInviteList
        invitedGroupAdapter.notifyDataSetChanged()

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