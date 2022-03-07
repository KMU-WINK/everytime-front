package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    var invitedBoolean = false

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

        okButton.setOnClickListener {
            InviteData.fixUsers = mutableListOf()
            InviteData.fixGroups = mutableListOf()
            InviteData.fixUsers.addAll(AddScheduleInfo.inviteMembers)
            InviteData.fixGroups.addAll(AddScheduleInfo.inviteGroups)
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
                ?.commit()
        }

        backButton.setOnClickListener {
            if (InviteData.fixUsers.size == 0 && InviteData.fixGroups.size == 0) {
                AddScheduleInfo.inviteMembers = mutableListOf()
                AddScheduleInfo.inviteGroups = mutableListOf()
                AddScheduleInfo.inviteGroupsNumber = 0
                AddScheduleInfo.invitersNumber = 0
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    AddScheduleInfo.followerList[i].invite = false
                }
                for (i in 0 until AddScheduleInfo.groupList.size) {
                    AddScheduleInfo.groupList[i].invite = false
                }
            } else if (InviteData.fixUsers.size > 0 && InviteData.fixGroups.size == 0) {
                AddScheduleInfo.inviteMembers = mutableListOf()
                AddScheduleInfo.inviteGroups = mutableListOf()
                AddScheduleInfo.inviteMembers.addAll(InviteData.fixUsers)
                AddScheduleInfo.inviteGroupsNumber = 0
                AddScheduleInfo.invitersNumber = InviteData.fixUsers.size
                val tempNumbers1 = mutableListOf(-1)
                for (i in 0 until InviteData.fixUsers.size) {
                    InviteData.fixUsers[i].sequence?.let { it1 ->
                        tempNumbers1.apply {
                            tempNumbers1.add(it1)
                        }
                    }
                }
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    if (AddScheduleInfo.followerList[i].sequence !in tempNumbers1) {
                        AddScheduleInfo.followerList[i].invite = false
                    }
                }
                for (i in 0 until AddScheduleInfo.groupList.size) {
                    AddScheduleInfo.groupList[i].invite = false
                }
            } else if (InviteData.fixUsers.size == 0 && InviteData.fixGroups.size > 0) {
                AddScheduleInfo.inviteMembers = mutableListOf()
                AddScheduleInfo.inviteGroups = mutableListOf()
                AddScheduleInfo.inviteGroups.addAll(InviteData.fixGroups)
                AddScheduleInfo.inviteGroupsNumber = InviteData.fixGroups.size
                AddScheduleInfo.invitersNumber = 0
                val tempNumbers2 = mutableListOf(-1)
                for (i in 0 until InviteData.fixGroups.size) {
                    InviteData.fixGroups[i].sequence?.let { it1 ->
                        tempNumbers2.apply {
                            tempNumbers2.add(it1)
                        }
                    }
                }
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    AddScheduleInfo.followerList[i].invite = false
                }
                for (i in 0 until AddScheduleInfo.groupList.size) {
                    if (AddScheduleInfo.groupList[i].sequence !in tempNumbers2) {
                        AddScheduleInfo.groupList[i].invite = false
                    }
                }
            } else {
                AddScheduleInfo.inviteMembers = mutableListOf()
                AddScheduleInfo.inviteGroups = mutableListOf()
                AddScheduleInfo.inviteGroups.addAll(InviteData.fixGroups)
                AddScheduleInfo.inviteMembers.addAll(InviteData.fixUsers)
                AddScheduleInfo.inviteGroupsNumber = InviteData.fixGroups.size
                AddScheduleInfo.invitersNumber = InviteData.fixUsers.size
                val tempNumbers1 = mutableListOf(-1)
                val tempNumbers2 = mutableListOf(-1)
                for (i in 0 until InviteData.fixUsers.size) {
                    InviteData.fixUsers[i].sequence?.let { it1 ->
                        tempNumbers1.apply {
                            tempNumbers1.add(it1)
                        }
                    }
                }
                for (i in 0 until InviteData.fixGroups.size) {
                    InviteData.fixGroups[i].sequence?.let { it1 ->
                        tempNumbers2.apply {
                            tempNumbers2.add(it1)
                        }
                    }
                }
                for (i in 0 until AddScheduleInfo.followerList.size) {
                    if (AddScheduleInfo.followerList[i].sequence !in tempNumbers1) {
                        AddScheduleInfo.followerList[i].invite = false
                    }
                }
                for (i in 0 until AddScheduleInfo.groupList.size) {
                    if (AddScheduleInfo.groupList[i].sequence !in tempNumbers1) {
                        AddScheduleInfo.groupList[i].invite = false
                    }
                }
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
                ?.addToBackStack(null)
                ?.commit()
        }

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
//                    Log.v("test7", tempInvitedList.toString())
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
//                    Log.v("test7", tempInvitedList.toString())
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
                if (data.user) {
                    AddScheduleInfo.followerList[data.sequence!!].invite = false
                    itemAdapter.datas = AddScheduleInfo.followerList
                    itemAdapter.notifyDataSetChanged()
                    AddScheduleInfo.inviteMembers.remove(data)
                    AddScheduleInfo.invitersNumber--
                    inviteList.remove(data)
                    invitedAdapter.datas = inviteList
                    invitedAdapter.notifyDataSetChanged()
                } else {
                    AddScheduleInfo.groupList[data.sequence!!].invite = false
                    groupAdapter.datas = AddScheduleInfo.groupList
                    groupAdapter.notifyDataSetChanged()
                    AddScheduleInfo.inviteGroups.remove(data)
                    AddScheduleInfo.inviteGroupsNumber--
                    AddScheduleInfo.allGroupMembersNumber -= data.isFav
                    inviteList.remove(data)
                    invitedAdapter.datas = inviteList
                    invitedAdapter.notifyDataSetChanged()
                }
                if (AddScheduleInfo.inviteGroupsNumber == 0 && AddScheduleInfo.invitersNumber == 0) {
                    invitedBoolean = false
                }
                if (!invitedBoolean) {
                    invited_recycler.visibility = View.GONE
                }
            }
        })

        groupAdapter.setOnArrowClickListener(object :
            Addschedule_invite_detail_Follower_Adapter.OnArrowClickListener {
            override fun onArrowClick(v: ImageView, data: UserModel, pos: Int) {
                val frag = AddSchedule_invite_detail_group()
                val bundle = Bundle()
                bundle.putString("id", data.id)
                frag.arguments = bundle
                parentFragment?.childFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.addschedule_frame, frag)
                    ?.addToBackStack(null)
                    ?.commit()
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