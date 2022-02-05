package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class AddSchedule_invite : Fragment() {
    lateinit var groupAdapter : Addschedule_invite_item_Adapter
    var groupInviteList = mutableListOf<UserModel>()
    lateinit var groupInviteView : RecyclerView
    lateinit var userAdapter: Addschedule_invite_item_Adapter
    var userInviteList = mutableListOf<UserModel>()
    lateinit var userInviteView: RecyclerView
    lateinit var groupFrame:LinearLayout
    lateinit var userFrame:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_invite, container, false)

        val okButton = view.findViewById<TextView>(R.id.invite_ok_button)
        val inviteTitle = view.findViewById<TextView>(R.id.invite_title)
        val inviteButton = view.findViewById<LinearLayout>(R.id.invite_button)
        groupFrame = view.findViewById(R.id.groups_frame)
        userFrame = view.findViewById(R.id.knockmates_frame)

        inviteTitle.text = "초대 " + AddScheduleInfo.invitersNumber.toString() + "명"

        okButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame,AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        inviteButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame,Addschedule_invite_detail())
                ?.addToBackStack(null)
                ?.commit()
//            childFragmentManager.beginTransaction().replace(R.id.frame,Addschedule_invite_detail()).commit()
        }


//        initGroupInviteList()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initGroupInviteList(){
        groupAdapter = Addschedule_invite_item_Adapter(requireContext())
        userAdapter = Addschedule_invite_item_Adapter(requireContext())
        groupInviteView.adapter = groupAdapter
        userInviteView.adapter = userAdapter
        var count1 = 0
        var count2 = 0

        for (i in 0 until AddScheduleInfo.inviteMembers.size){
            if(AddScheduleInfo.inviteMembers[i].invite){
                if(AddScheduleInfo.inviteMembers[i].user){
                    count1++
                    if(count1==1){
                        userFrame.visibility = View.VISIBLE
                    }
                    userInviteList.apply {
                        userInviteList.add(AddScheduleInfo.inviteMembers[i])
                        userAdapter.datas = userInviteList
                        userAdapter.notifyDataSetChanged()
                    }
                }else{
                    count2++
                    if(count2==1){
                        groupFrame.visibility = View.VISIBLE
                    }
                    groupInviteList.apply {
                        groupInviteList.add(AddScheduleInfo.inviteMembers[i])
                        groupAdapter.datas = groupInviteList
                        groupAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }
}