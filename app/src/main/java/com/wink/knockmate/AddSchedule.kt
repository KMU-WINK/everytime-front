package com.wink.knockmate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class AddSchedule : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
//    @SuppressLint("CommitPrefEdits")
//    override fun onStart() {
//        super.onStart()
//        val bundle = arguments
//        AddScheduleInfo()
//        val args = bundle?.getString("ScheduleType")
//        if(args == "Add"){
//            val prefUser = activity?.getSharedPreferences("LoginInfo", MODE_PRIVATE)
//            val client = OkHttpClient().newBuilder()
//                .build()
//            val request: Request = Request.Builder()
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .url("http://3.35.146.57:3000/user?query=dy@${prefUser?.getString("email", "email")}")
//                .get()
//                .build()
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d("log1", e.message.toString())
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    object : Thread() {
//                        @SuppressLint("NotifyDataSetChanged")
//                        override fun run() {
//                            if (response.code == 200) {
//                                val res = JSONObject(response.body?.string())
//                                val resTemp = res.getJSONArray("data")
//                                AddScheduleInfo.userEmail = resTemp.getJSONObject(0).getString("email")
//                                AddScheduleInfo.userId = resTemp.getJSONObject(0).getString("id")
//                                AddScheduleInfo.color = resTemp.getJSONObject(0).getInt("color")
//                            } else {
//                                dismiss()
//                                Toast.makeText(context, "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
//                            }
//                        }
//                    }.run()
//                }
//            })
//        }
//    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState)

        val view = View.inflate(context, R.layout.addschedule, null)
//        view.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        bottomSheet.setContentView(view)

//        val transition = Fade()
//        transition.setDuration(200)

//        val frame = view.findViewById<FrameLayout>(R.id.addschedule_frame)
//        val brief = view.findViewById<FrameLayout>(R.id.brief_frame)
//        val detail = view.findViewById<FrameLayout>(R.id.detail_frame)
//        val brief_setting = view.findViewById<ConstraintLayout>(R.id.brief_dateNTime)
////        val space = view.findViewById<View>(R.id.space)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

//        val mainScheduleFrame = view.findViewById<ConstraintLayout>(R.id.main_schedule_frame)
//
//        brief_setting.setOnClickListener(View.OnClickListener {
//            transition.addTarget(R.id.detail_frame)
//            TransitionManager.beginDelayedTransition(brief, transition)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }) // brief setting 누르고 state를 STATE_EXPANDED로 바꾸는데 자연스럽게 바뀌게 하기

//        bottomSheetBehavior.peekHeight = 400

        childFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.addschedule_frame, AddSchedule_brief())
            .commit()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .addToBackStack(null)
                        .replace(R.id.addschedule_frame, AddSchedule_brief())
                        .commit()

//                    brief.visibility = View.VISIBLE
//                    frame.visibility = View.INVISIBLE
//                    detail.visibility = View.INVISIBLE
////                    space.visibility = View.INVISIBLE
                }
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .addToBackStack(null)
                        .replace(R.id.addschedule_frame, AddSchedule_detail())
                        .commit()
//                    brief.visibility = View.INVISIBLE
//                    detail.visibility = View.VISIBLE
////                    space.visibility = View.VISIBLE
                }
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })


        return bottomSheet
    }
}