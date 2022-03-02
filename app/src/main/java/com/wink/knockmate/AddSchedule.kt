package com.wink.knockmate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.util.AndroidException
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.Exception

class AddSchedule : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    @SuppressLint("CommitPrefEdits")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AddScheduleInfo.reset()
        val bundle = arguments
        val args = bundle?.getString("ScheduleType")
        if (args == "ADD") {
            val prefUser = activity?.getSharedPreferences("LoginInfo", MODE_PRIVATE)
            val email = prefUser?.getString("email", "email")
            prefUser?.getString(
                "email",
                "email"
            )?.let { Log.v("test", it) }
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(
                    "http://3.35.146.57:3000/user?query=${email}"
                )
                .get()
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                Log.v("test", res.toString())
                                try {
                                    val resTemp = res.getJSONArray("data")
                                    AddScheduleInfo.userEmail =
                                        resTemp.getJSONObject(0).getString("email")
                                    AddScheduleInfo.userId =
                                        resTemp.getJSONObject(0).getString("id")
                                    AddScheduleInfo.color = resTemp.getJSONObject(0).getInt("color")
                                } catch (e: Exception) {
                                    dismiss()
                                    Toast
                                        .makeText(
                                            context,
                                            "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            } else {
                                dismiss()
                                Toast
                                    .makeText(
                                        context,
                                        "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        }
                    }.run()
                }
            })
            AddScheduleInfo.resetStartCal()
            AddScheduleInfo.startDay = dayOfWeek(AddScheduleInfo.startCal.get(Calendar.DAY_OF_WEEK))
            AddScheduleInfo.endDay = dayOfWeek(AddScheduleInfo.endCal.get(Calendar.DAY_OF_WEEK))

            val request2: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/myfollower?email=${email}")
                .get()
                .build()
            client.newCall(request2).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                val resTemp = res.getJSONArray("data")
                                AddScheduleInfo.followerList.apply {
                                    for (i in 0 until resTemp.length()) {
                                        AddScheduleInfo.followerList.add(
                                            UserModel(
                                                resTemp.getJSONObject(i).getString("id"),
                                                resTemp.getJSONObject(i).getString("nickname"),
                                                true,
                                                resTemp.getJSONObject(i).getString("email"),
                                                i
                                            )
                                        )
                                    }
                                }
                            } else if (response.code() == 201) {
                            } else {
                            }
                        }
                    }.run()
                }
            })
        } else if (args == "Modify") {
            //캘린더에서 받아오기
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState)

        val view = View.inflate(context, R.layout.addschedule, null)
//        view.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        bottomSheet.setContentView(view)

        val transition = Fade()
        transition.duration = 200

        val frame = view.findViewById<FrameLayout>(R.id.addschedule_frame)
//        val brief = view.findViewById<FrameLayout>(R.id.brief_frame)
//        val detail = view.findViewById<FrameLayout>(R.id.detail_frame)
//        val brief_setting = view.findViewById<ConstraintLayout>(R.id.brief_dateNTime)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

//        if(AddScheduleInfo.brief){
//            brief.visibility = View.VISIBLE
//            detail.visibility = View.GONE
//        }else{
//            brief.visibility = View.GONE
//            detail.visibility = View.VISIBLE
//        }

//        val mainScheduleFrame = view.findViewById<ConstraintLayout>(R.id.main_schedule_frame)

//        brief_setting.setOnClickListener(View.OnClickListener {
//            transition.addTarget(R.id.detail_frame)
//            TransitionManager.beginDelayedTransition(brief, transition)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }) // brief setting 누르고 state를 STATE_EXPANDED로 바꾸는데 자연스럽게 바뀌게 하기

        bottomSheetBehavior.peekHeight = 400

        childFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.addschedule_frame, AddSchedule_brief())
            .commit()

//        childFragmentManager.beginTransaction().addToBackStack(null)
//            .replace(R.id.detail_frame, AddSchedule_detail()).commit()


//        val addscheduleIcon = view.findViewById<ImageView>(R.id.addschedule_icon)
//        colorSetting(addscheduleIcon)
//
//        val startDateText = view.findViewById<TextView>(R.id.detail_start_date)
//        val startTimeText = view.findViewById<TextView>(R.id.detail_start_time)
//        val endDateText = view.findViewById<TextView>(R.id.detail_end_date)
//        val endTimeText = view.findViewById<TextView>(R.id.detail_end_time)
//        val startCal = AddScheduleInfo.startCal
//        val endCal = AddScheduleInfo.endCal
//
//        startDateText.text = (startCal.get(Calendar.MONTH) + 1).toString() + "월" + startCal.get(
//            Calendar.DATE
//        ).toString() + "일 (" + AddScheduleInfo.startDay + ")"
//        startTimeText.text =
//            if (startCal.get(Calendar.AM_PM) === 0) "오전" + " " + startCal.get(Calendar.HOUR)
//                .toString() + ":" + startCal.get(
//                Calendar.MINUTE
//            ).toString()
//            else "오후" + " " + startCal.get(Calendar.HOUR)
//                .toString() + ":" + startCal.get(Calendar.MINUTE).toString()
//        endDateText.text =
//            (endCal.get(Calendar.MONTH) + 1).toString() + "월" + endCal.get(Calendar.DATE)
//                .toString() + "일 (" + AddScheduleInfo.endDay + ")"
//        endTimeText.text =
//            if (startCal.get(Calendar.AM_PM) === 0) "오전" + " " + endCal.get(Calendar.HOUR)
//                .toString() + ":" + endCal.get(
//                Calendar.MINUTE
//            ).toString()
//            else "오후" + " " + endCal.get(Calendar.HOUR)
//                .toString() + ":" + endCal.get(Calendar.MINUTE).toString()
//
//
//        val startDateText_brief = view.findViewById<TextView>(R.id.brief_date)
//        val startTimeText_brief = view.findViewById<TextView>(R.id.brief_start_time)
//        val endTimeText_brief = view.findViewById<TextView>(R.id.brief_end_time)
//
//        startDateText_brief.text =
//            (startCal.get(Calendar.MONTH) + 1).toString() + "." + startCal.get(
//                Calendar.DATE
//            ).toString() + " " + AddScheduleInfo.startDay + "요일"
//        startTimeText_brief.text =
//            if (startCal.get(Calendar.AM_PM) === 0) "오전" + " " + startCal.get(
//                Calendar.HOUR
//            ).toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
//            else "오후" + " " + startCal.get(Calendar.HOUR)
//                .toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
//        endTimeText_brief.text =
//            if (endCal.get(Calendar.AM_PM) === 0) "오전" + " " + endCal.get(Calendar.HOUR)
//                .toString() + "시 " + endCal.get(
//                Calendar.MINUTE
//            ).toString() + "분"
//            else "오후" + " " + endCal.get(Calendar.HOUR)
//                .toString() + "시 " + endCal.get(Calendar.MINUTE).toString() + "분"
//
//
//        val startView = view.findViewById<LinearLayout>(R.id.addschedule_start)
//        val startPicker = view.findViewById<ConstraintLayout>(R.id.start_picker)
//        val startDatePicker = view.findViewById<NumberPicker>(R.id.start_date_picker)
//        val startAmOrPmPicker = view.findViewById<NumberPicker>(R.id.start_am_or_pm)
//        val startTimePicker = view.findViewById<NumberPicker>(R.id.start_time_picker)
//        val month_list =
//            arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
//        val day_list1 = arrayOf(
//            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일", "13일",
//            "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일", "24일", "25일",
//            "26일", "27일", "28일", "29일", "30일", "31일"
//        )
//        val day_list2 = arrayOf(
//            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
//            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
//            "24일", "25일", "26일", "27일", "28일", "29일", "30일"
//        )
//        val day_list3 = arrayOf(
//            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
//            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
//            "24일", "25일", "26일", "27일", "28일"
//        )
//        val day_list4 = arrayOf(
//            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
//            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
//            "24일", "25일", "26일", "27일", "28일", "29일"
//        )
//        val temp = mutableListOf<String>()
//        for (i in month_list) {
//            if (i === "1월" || i === "3월" || i === "5월" || i === "7월" || i === "8월" || i === "10월" || i === "12월") {
//                for (j in day_list1)
//                    temp.add(i + j)
//            } else if (i === "2월") {
//                for (j in day_list3)
//                    temp.add(i + j)
//            } else
//                for (j in day_list2)
//                    temp.add(i + j)
//        }
//        val hourAm = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
//        val hourPm = arrayOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
//        val minute = arrayOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
//        val temp2 = mutableListOf<String>()
//        for (i in hourAm) {
//            for (j in minute)
//                temp2.add(i.toString() + ":" + j.toString())
//        }
//        for (i in hourPm) {
//            for (j in minute)
//                temp2.add(i.toString() + ":" + j.toString())
//        }
//        // 윤년 케이스 추가해야함
//        startDatePicker.displayedValues = temp.toTypedArray()
//        startAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
//        startTimePicker.displayedValues = temp2.toTypedArray()
//        startDatePicker.value = (startCal.get(Calendar.MONTH) + 1).toString().toInt()
//        if (startCal.get(Calendar.AM_PM) == Calendar.AM) {
//            startAmOrPmPicker.value = 0
//        } else {
//            startAmOrPmPicker.value = 1
//        }
//
//        val endPicker = view.findViewById<ConstraintLayout>(R.id.end_picker)
//
//        startView.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.VISIBLE
//            endPicker.visibility = View.GONE
//        })
//
//
//        // endpicker
//        val endView = view.findViewById<LinearLayout>(R.id.addschedule_end)
//        val endDatePicker = view.findViewById<NumberPicker>(R.id.end_date_picker)
//        val endAmOrPmPicker = view.findViewById<NumberPicker>(R.id.end_am_or_pm)
//        val endTimePicker = view.findViewById<NumberPicker>(R.id.end_time_picker)
//        // 윤년 케이스 추가해야함
//        endDatePicker.displayedValues = temp.toTypedArray()
//        endAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
//        endTimePicker.displayedValues = temp2.toTypedArray()
//        endDatePicker.value = (endCal.get(Calendar.MONTH) + 1).toString().toInt()
//        if (endCal.get(Calendar.AM_PM) == Calendar.AM) {
//            endAmOrPmPicker.value = 0
//        } else {
//            endAmOrPmPicker.value = 1
//        }
//
//        endView.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.VISIBLE
//        })
//
//
//        // 일정 제목
//        val title = view.findViewById<EditText>(R.id.schedule_title)
//        var titleClick = false
//        title.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                if (!titleClick || AddScheduleInfo.title == "일정 제목") {
//                    title.text = null
//                }
//                titleClick = true
//                startPicker.visibility = View.GONE
//                endPicker.visibility = View.GONE
//            } else {
//                if (AddScheduleInfo.title != null || AddScheduleInfo.title == "일정 제목") {
//                    title.setText(AddScheduleInfo.title)
//                }
//            }
//        }
//        title.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                AddScheduleInfo.title = title.text.toString()
//            }
//        })
//
//        // 일정 메모
//        val memo = view.findViewById<EditText>(R.id.memo)
//        memo.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                startPicker.visibility = View.GONE
//                endPicker.visibility = View.GONE
//            }
//        }
//        memo.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                AddScheduleInfo.memo = memo.text.toString()
//            }
//        })
//
//
//        // 참석자초대로 넘어가는 버튼
//        val inviters = view.findViewById<TextView>(R.id.inviters)
//        if (AddScheduleInfo.invitersNumber == 0) {
//            inviters.text = ""
//        } else if (AddScheduleInfo.invitersNumber == 1) {
//            inviters.text = AddScheduleInfo.inviteMembers[0].nickname
//        } else {
//            inviters.text =
//                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber - 1).toString() + "명"
//        }
//        val to_invite_button = view.findViewById<ConstraintLayout>(R.id.to_invite_button)
//        to_invite_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            frame.visibility=View.VISIBLE
//            parentFragment?.childFragmentManager
//                ?.beginTransaction()
//                ?.setCustomAnimations(
//                    R.anim.fade_in,
//                    R.anim.fade_out,
//                    R.anim.fade_in,
//                    R.anim.fade_out
//                )
//                ?.addToBackStack(null)
//                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
//                ?.commit()
//        })
//
//
//        // 반복 일정으로 넘어가는 버튼
//        val repeatText = view.findViewById<TextView>(R.id.repeat_text)
//        repeatText.text = AddScheduleInfo.repeatType
//        val to_repeat_button = view.findViewById<ConstraintLayout>(R.id.to_repeat_button)
//        to_repeat_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            parentFragment?.childFragmentManager
//                ?.beginTransaction()
//                ?.setCustomAnimations(
//                    R.anim.fade_in,
//                    R.anim.fade_out,
//                    R.anim.fade_in,
//                    R.anim.fade_out
//                )
//                ?.addToBackStack(null)
//                ?.replace(R.id.addschedule_frame, AddSchedule_repeat())
//                ?.commit()
//        })
//
//
//        // 일정 색상 선택으로 넘어가는 버튼
//        val to_colorPick_button = view.findViewById<ConstraintLayout>(R.id.to_colorPick_button)
//        val colorName = view.findViewById<TextView>(R.id.pick_color_name)
//        val colorIcon = view.findViewById<ImageView>(R.id.color_icon)
//        colorName.text = "색상" + AddScheduleInfo.color.toString()
//        colorSetting(colorIcon)
//        frame.visibility=View.VISIBLE
//        to_colorPick_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            detail.visibility = View.GONE
//            frame.visibility = View.VISIBLE
//            childFragmentManager
//                .beginTransaction()
//                .setCustomAnimations(
//                    R.anim.fade_in,
//                    R.anim.fade_out,
//                    R.anim.fade_in,
//                    R.anim.fade_out
//                ).addToBackStack(null)
//                .replace(R.id.addschedule_frame, AddSchedule_colorPick())
//                .commit()
//        })


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
                        .replace(R.id.addschedule_frame, AddSchedule_brief())
                        .commit()

                    AddScheduleInfo.brief = true

//                    brief.visibility = View.VISIBLE
//                    frame.visibility = View.INVISIBLE
//                    detail.visibility = View.INVISIBLE
//                    AddScheduleInfo.brief = true
////                    space.visibility = View.INVISIBLE
                }
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    if (AddScheduleInfo.brief) {
                        childFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.fade_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            .replace(R.id.addschedule_frame, AddSchedule_detail())
                            .commit()
                    }

                    AddScheduleInfo.brief = false

//                    brief.visibility = View.INVISIBLE
//                    detail.visibility = View.VISIBLE
//                    AddScheduleInfo.brief = false
////                    space.visibility = View.VISIBLE
                }
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                    if (brief.visibility == View.VISIBLE) {
////                        transition.addTarget(R.id.detail_frame)
////                        TransitionManager.beginDelayedTransition(brief, transition)
////                        brief.visibility = View.INVISIBLE
////                        detail.visibility = View.VISIBLE
//                    } else if (detail.visibility == View.VISIBLE) {
////                        transition.addTarget(R.id.brief_frame)
////                        TransitionManager.beginDelayedTransition(detail, transition)
////                        brief.visibility = View.VISIBLE
////                        detail.visibility = View.INVISIBLE
////                        frame.visibility = View.INVISIBLE
//                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })


        return bottomSheet
    }


    private fun colorSetting(v: ImageView) {
        if (AddScheduleInfo.color == 1) {
            Glide.with(this).load(R.drawable.color1).into(v)
        } else if (AddScheduleInfo.color == 2) {
            Glide.with(this).load(R.drawable.color2).into(v)
        } else if (AddScheduleInfo.color == 3) {
            Glide.with(this).load(R.drawable.color3).into(v)
        } else if (AddScheduleInfo.color == 4) {
            Glide.with(this).load(R.drawable.color4).into(v)
        } else if (AddScheduleInfo.color == 5) {
            Glide.with(this).load(R.drawable.color5).into(v)
        } else if (AddScheduleInfo.color == 6) {
            Glide.with(this).load(R.drawable.color6).into(v)
        } else if (AddScheduleInfo.color == 7) {
            Glide.with(this).load(R.drawable.color7).into(v)
        } else if (AddScheduleInfo.color == 8) {
            Glide.with(this).load(R.drawable.color8).into(v)
        } else if (AddScheduleInfo.color == 9) {
            Glide.with(this).load(R.drawable.color9).into(v)
        } else if (AddScheduleInfo.color == 10) {
            Glide.with(this).load(R.drawable.color10).into(v)
        } else if (AddScheduleInfo.color == 11) {
            Glide.with(this).load(R.drawable.color11).into(v)
        } else if (AddScheduleInfo.color == 12) {
            Glide.with(this).load(R.drawable.color12).into(v)
        }
    }

    private fun dayOfWeek(d: Int): String {
        return when (d) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> " "
        }
    }
}