package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Addschedule_invite_detail : Fragment() {
    lateinit var itemAdapter: Addschedule_invite_detail_Follower_Adapter
    lateinit var knockmate_recycler: RecyclerView

    lateinit var groupAdapter: Addschedule_invite_detail_Follower_Adapter
    lateinit var group_recycler: RecyclerView

    lateinit var invitedAdapter: Addschedule_invited_item_Adapter
    var inviteList = mutableListOf<UserModel>()
    lateinit var invited_recycler: RecyclerView

    var tempInvitedList = mutableListOf<UserModel>()
    private var tempInvitedNumber = 0
    private var tempInvitedList2 = mutableListOf<UserModel>()

    var invitedBoolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        for (i in 0 until tempInvitedList.size) {
//            var temp = tempInvitedList[i]
//            tempInvitedList2.add(temp)
//        }
        for (i in 0 until AddScheduleInfo.inviteMembers.size) {
            tempInvitedList.add(AddScheduleInfo.inviteMembers[i].copy())
        }
        Log.v("test6", tempInvitedList.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.addschedule_invite_detail, container, false)

        val okButton = view.findViewById<TextView>(R.id.invite_ok_button)
        val backButton = view.findViewById<View>(R.id.back_button)
        knockmate_recycler = view.findViewById(R.id.invite_knockmate_recycler)
        invited_recycler = view.findViewById(R.id.to_invite_users)
        group_recycler = view.findViewById(R.id.invite_group_member_recycler)

        tempInvitedList = AddScheduleInfo.inviteMembers
        tempInvitedNumber = AddScheduleInfo.invitersNumber

        okButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
                ?.commit()
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
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
                ?.addToBackStack(null)
                ?.commit()
        }

        Log.v("followers", AddScheduleInfo.followerList.toString())
        Log.v("test4", AddScheduleInfo.inviteMembers.toString())
        Log.v("test2", tempInvitedNumber.toString())
        Log.v("test3", tempInvitedList.toString())

        initInvitedRecycler()
        initFollowerRecycler()
        initGroupRecycler()

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

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupRecycler() {
        groupAdapter =
            Addschedule_invite_detail_Follower_Adapter()
        group_recycler.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        group_recycler.adapter = groupAdapter
        groupAdapter.datas = AddScheduleInfo.groupList
        groupAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initInvitedRecycler() {
        invitedAdapter =
            Addschedule_invited_item_Adapter(requireParentFragment().requireActivity().applicationContext)
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
    private fun initFollowerRecycler() {
        itemAdapter =
            Addschedule_invite_detail_Follower_Adapter()
        knockmate_recycler.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        knockmate_recycler.adapter = itemAdapter

        itemAdapter.datas = AddScheduleInfo.followerList
        itemAdapter.notifyDataSetChanged()
    }
}