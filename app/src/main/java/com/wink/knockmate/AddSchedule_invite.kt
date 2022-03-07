package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddSchedule_invite : Fragment() {
    lateinit var groupAdapter: Addschedule_invite_item_Adapter
    lateinit var groupInviteView: RecyclerView
    lateinit var userAdapter: Addschedule_invite_item_Adapter
    lateinit var userInviteView: RecyclerView
    lateinit var groupFrame: LinearLayout
    lateinit var userFrame: LinearLayout

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_invite, container, false)

        val okButton = view.findViewById<TextView>(R.id.invite_ok_button)
        val backButton = view.findViewById<View>(R.id.back_button)
        val inviteTitle = view.findViewById<TextView>(R.id.invite_title)
        val inviteButton = view.findViewById<LinearLayout>(R.id.invite_button)
        groupInviteView = view.findViewById(R.id.group_recycler)
        userInviteView = view.findViewById(R.id.knockmates_recycler)
        groupFrame = view.findViewById(R.id.groups_frame)
        userFrame = view.findViewById(R.id.knockmates_frame)

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
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
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
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        inviteButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, Addschedule_invite_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        initGroupInviteList()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
                parentFragment?.childFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }

        requireParentFragment().requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupInviteList() {
        groupAdapter =
            Addschedule_invite_item_Adapter()
        userAdapter =
            Addschedule_invite_item_Adapter()
        groupInviteView.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        userInviteView.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        groupInviteView.adapter = groupAdapter
        userInviteView.adapter = userAdapter


        userAdapter.datas = AddScheduleInfo.inviteMembers
        userAdapter.notifyDataSetChanged()

        groupAdapter.datas = AddScheduleInfo.inviteGroups
        groupAdapter.notifyDataSetChanged()

        if (AddScheduleInfo.inviteMembers.size >= 1) {
            userFrame.visibility = View.VISIBLE
        }

        if (AddScheduleInfo.inviteGroups.size >= 1) {
            groupFrame.visibility = View.VISIBLE
        }

//        userAdapter.datas = userInviteList
//        userAdapter.notifyDataSetChanged()
//        groupAdapter.datas = groupInviteList
//        groupAdapter.notifyDataSetChanged()
    }
}