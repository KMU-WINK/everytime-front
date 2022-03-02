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

    lateinit var groupAdapter: Addschedule_invite_detail_group_Adapter
    var groupList = mutableListOf<UserModel>()
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
        knockmate_recycler = view.findViewById(R.id.invite_knockmate_recycler)
        invited_recycler = view.findViewById(R.id.to_invite_users)
        group_recycler = view.findViewById(R.id.invite_group_member_recycler)

        okButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
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
                AddScheduleInfo.followerList[pos].invite = v.isChecked
                if (AddScheduleInfo.followerList[pos].invite) {
                    AddScheduleInfo.invitersNumber++
                    AddScheduleInfo.inviteMembers.add(AddScheduleInfo.followerList[pos])
                    inviteList.apply {
                        inviteList.add(AddScheduleInfo.followerList[pos])
                    }
                    if (inviteList.size == 1) {
                        invitedBoolean = true
                    }
                } else {
                    AddScheduleInfo.invitersNumber--
                    inviteList.apply {
                        AddScheduleInfo.inviteMembers.remove(AddScheduleInfo.followerList[pos])
                        inviteList.remove(AddScheduleInfo.followerList[pos])
                    }
                    if (inviteList.size == 0) {
                        invitedBoolean = false
                    }
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
            Addschedule_invite_detail_group_Adapter(requireParentFragment().requireActivity().applicationContext)
        group_recycler.layoutManager =
            LinearLayoutManager(requireParentFragment().requireActivity().applicationContext)
        group_recycler.adapter = groupAdapter
        groupList.apply {
            groupAdapter.datas = groupList
            groupAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initInvitedRecycler() {
        invitedAdapter =
            Addschedule_invited_item_Adapter(requireParentFragment().requireActivity().applicationContext)
        invited_recycler.adapter = invitedAdapter
        if(AddScheduleInfo.invitersNumber == 0){
            invited_recycler.visibility = View.GONE
        }else{
            invited_recycler.visibility = View.VISIBLE
        }
        inviteList.apply {
            for (i in 0 until AddScheduleInfo.inviteMembers.size) {
                inviteList.add(AddScheduleInfo.followerList[i])
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