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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddSchedule_invite : Fragment() {
    lateinit var groupAdapter: Addschedule_invite_item_Adapter
    var groupInviteList = mutableListOf<UserModel>()
    lateinit var groupInviteView: RecyclerView
    lateinit var userAdapter: Addschedule_invite_item_Adapter
    var userInviteList = mutableListOf<UserModel>()
    lateinit var userInviteView: RecyclerView
    lateinit var groupFrame: LinearLayout
    lateinit var userFrame: LinearLayout

    var count1 = 0
    var count2 = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        for (i in 0 until AddScheduleInfo.inviteMembers.size) {
            if (AddScheduleInfo.inviteMembers[i].invite) {
                if (AddScheduleInfo.inviteMembers[i].user) {
                    count1++
                    userInviteList.apply {
                        userInviteList.add(AddScheduleInfo.inviteMembers[i])
                    }
                } else {
                    count2++
                    groupInviteList.apply {
                        groupInviteList.add(AddScheduleInfo.inviteMembers[i])
                    }
                }
            }
        }
    }

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

        inviteTitle.text = "초대 " + AddScheduleInfo.invitersNumber.toString() + "명"

        okButton.setOnClickListener {
            AddScheduleInfo.priorInviteMembers = AddScheduleInfo.inviteMembers
            AddScheduleInfo.priorInvitersNumber = AddScheduleInfo.invitersNumber
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        backButton.setOnClickListener {
            AddScheduleInfo.inviteMembers = AddScheduleInfo.priorInviteMembers
            val tempNumbers = mutableListOf(-1)
            for (i in 0 until AddScheduleInfo.priorInviteMembers.size) {
                AddScheduleInfo.priorInviteMembers[i].sequence?.let { it1 ->
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
            AddScheduleInfo.invitersNumber = AddScheduleInfo.priorInvitersNumber
            AddScheduleInfo.inviteMembers = AddScheduleInfo.priorInviteMembers
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

        Log.v("test", userInviteList.toString())
        Log.v("group", groupInviteList.toString())
        Log.v("number", AddScheduleInfo.invitersNumber.toString())


        initGroupInviteList()

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

        for (i in 0 until AddScheduleInfo.inviteMembers.size) {
            if (AddScheduleInfo.inviteMembers[i].invite) {
                if (AddScheduleInfo.inviteMembers[i].user) {
                    count1++
                    userInviteList.apply {
                        userInviteList.add(AddScheduleInfo.inviteMembers[i])
                    }
                    userAdapter.datas = userInviteList
                    userAdapter.notifyDataSetChanged()
                } else {
                    count2++
                    groupInviteList.apply {
                        groupInviteList.add(AddScheduleInfo.inviteMembers[i])
                    }
                    groupAdapter.datas = groupInviteList
                    groupAdapter.notifyDataSetChanged()
                }
            }
        }

        if (count1 >= 1) {
            userFrame.visibility = View.VISIBLE
        }

        if (count2 >= 1) {
            groupFrame.visibility = View.VISIBLE
        }

//        userAdapter.datas = userInviteList
//        userAdapter.notifyDataSetChanged()
//        groupAdapter.datas = groupInviteList
//        groupAdapter.notifyDataSetChanged()
    }
}