package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.net.URL
import java.util.*

class AddSchedule_detail : Fragment() {
    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.addschedule_detail, container, false)

        val addscheduleIcon = view.findViewById<ImageView>(R.id.addschedule_icon)
        colorSetting(addscheduleIcon)

        val startDateText = view.findViewById<TextView>(R.id.detail_start_date)
        val startTimeText = view.findViewById<TextView>(R.id.detail_start_time)
        val endDateText = view.findViewById<TextView>(R.id.detail_end_date)
        val endTimeText = view.findViewById<TextView>(R.id.detail_end_time)
        val startCal = AddScheduleInfo.startCal
        val endCal = AddScheduleInfo.endCal

        startDateText.text = (startCal.get(Calendar.MONTH) + 1).toString() + "월" + startCal.get(
            Calendar.DATE
        ).toString() + "일 (" + AddScheduleInfo.startDay + ")"
        startTimeText.text =
            if (startCal.get(Calendar.AM_PM) === 0) "오전" + " " + startCal.get(Calendar.HOUR)
                .toString() + ":" + startCal.get(
                Calendar.MINUTE
            ).toString()
            else "오후" + " " + startCal.get(Calendar.HOUR)
                .toString() + ":" + startCal.get(Calendar.MINUTE).toString()
        endDateText.text =
            (endCal.get(Calendar.MONTH) + 1).toString() + "월" + endCal.get(Calendar.DATE)
                .toString() + "일 (" + AddScheduleInfo.endDay + ")"
        endTimeText.text =
            if (startCal.get(Calendar.AM_PM) === 0) "오전" + " " + endCal.get(Calendar.HOUR)
                .toString() + ":" + endCal.get(
                Calendar.MINUTE
            ).toString()
            else "오후" + " " + endCal.get(Calendar.HOUR)
                .toString() + ":" + endCal.get(Calendar.MINUTE).toString()


        // startpicker
        val startView = view.findViewById<LinearLayout>(R.id.addschedule_start)
        val startPicker = view.findViewById<ConstraintLayout>(R.id.start_picker)
        val startDatePicker = view.findViewById<NumberPicker>(R.id.start_date_picker)
        val startAmOrPmPicker = view.findViewById<NumberPicker>(R.id.start_am_or_pm)
        val startTimePicker = view.findViewById<NumberPicker>(R.id.start_time_picker)
        val month_list =
            arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        val day_list1 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일", "29일", "30일", "31일"
        )
        val day_list2 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일", "29일", "30일"
        )
        val day_list3 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일"
        )
        val day_list4 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일",
            "23일", "24일", "25일", "26일", "27일", "28일", "29일"
        )
        val temp = mutableListOf<String>()
        for (i in month_list) {
            if (i === "1월" || i === "3월" || i === "5월" || i === "7월" || i === "8월" || i === "10월" || i === "12월") {
                for (j in day_list1)
                    temp.add(i + j)
            } else if (i === "2월") {
                for (j in day_list3)
                    temp.add(i + j)
            } else
                for (j in day_list2)
                    temp.add(i + j)
        }
        val hourAm = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val hourPm = arrayOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val minute = arrayOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
        val temp2 = mutableListOf<String>()
        for (i in hourAm) {
            for (j in minute)
                temp2.add(i.toString() + ":" + j.toString())
        }
        for (i in hourPm) {
            for (j in minute)
                temp2.add(i.toString() + ":" + j.toString())
        }
        // 윤년 케이스 추가해야함
        startDatePicker.displayedValues = temp.toTypedArray()
        startAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
        startTimePicker.displayedValues = temp2.toTypedArray()
        startDatePicker.value = (startCal.get(Calendar.MONTH) + 1).toString().toInt()
        if (startCal.get(Calendar.AM_PM) == Calendar.AM) {
            startAmOrPmPicker.value = 0
        } else {
            startAmOrPmPicker.value = 1
        }

        val endPicker = view.findViewById<ConstraintLayout>(R.id.end_picker)

        startView.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.VISIBLE
            endPicker.visibility = View.GONE
        })


        // endpicker
        val endView = view.findViewById<LinearLayout>(R.id.addschedule_end)
        val endDatePicker = view.findViewById<NumberPicker>(R.id.end_date_picker)
        val endAmOrPmPicker = view.findViewById<NumberPicker>(R.id.end_am_or_pm)
        val endTimePicker = view.findViewById<NumberPicker>(R.id.end_time_picker)
        // 윤년 케이스 추가해야함
        endDatePicker.displayedValues = temp.toTypedArray()
        endAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
        endTimePicker.displayedValues = temp2.toTypedArray()
        endDatePicker.value = (endCal.get(Calendar.MONTH) + 1).toString().toInt()
        if (endCal.get(Calendar.AM_PM) == Calendar.AM) {
            endAmOrPmPicker.value = 0
        } else {
            endAmOrPmPicker.value = 1
        }

        endView.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.VISIBLE
        })


        // 일정 제목
        val title = view.findViewById<EditText>(R.id.schedule_title)
        var titleClick = false
        title.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (!titleClick || AddScheduleInfo.title == "일정 제목") {
                        title.text = null
                    }
                    titleClick = true
                    startPicker.visibility = View.GONE
                    endPicker.visibility = View.GONE
                } else {
                    if (AddScheduleInfo.title != null || AddScheduleInfo.title == "일정 제목") {
                        title.setText(AddScheduleInfo.title)
                    }
                }
            }
        })
        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                AddScheduleInfo.title = title.text.toString()
            }
        })


        // 일정 메모
        val memo = view.findViewById<EditText>(R.id.memo)
        memo.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    startPicker.visibility = View.GONE
                    endPicker.visibility = View.GONE
                }
            }

        })
        memo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                AddScheduleInfo.memo = memo.text.toString()
            }
        })


        // 참석자초대로 넘어가는 버튼
        val inviters = view.findViewById<TextView>(R.id.inviters)
        if (AddScheduleInfo.invitersNumber == 0) {
            inviters.text = ""
        } else if (AddScheduleInfo.invitersNumber == 1) {
            inviters.text = AddScheduleInfo.inviteMembers[0].nickname
        } else {
            inviters.text =
                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber - 1).toString() + "명"
        }
        val to_invite_button = view.findViewById<ConstraintLayout>(R.id.to_invite_button)
        to_invite_button.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.GONE
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                ?.addToBackStack(null)
                ?.replace(R.id.addschedule_frame, AddSchedule_invite())
                ?.commit()
        })


        // 반복 일정으로 넘어가는 버튼
        val repeatText = view.findViewById<TextView>(R.id.repeat_text)
        repeatText.text = AddScheduleInfo.repeatType
        val to_repeat_button = view.findViewById<ConstraintLayout>(R.id.to_repeat_button)
        to_repeat_button.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.GONE
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                ?.addToBackStack(null)
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat())
                ?.commit()
        })


        // 일정 색상 선택으로 넘어가는 버튼
        val to_colorPick_button = view.findViewById<ConstraintLayout>(R.id.to_colorPick_button)
        val colorName = view.findViewById<TextView>(R.id.pick_color_name)
        val colorIcon = view.findViewById<ImageView>(R.id.color_icon)
        colorName.text = "색상" + AddScheduleInfo.color.toString()
        colorSetting(colorIcon)
        to_colorPick_button.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.GONE
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                ?.addToBackStack(null)
                ?.replace(R.id.addschedule_frame, AddSchedule_colorPick())
                ?.commit()
        })


        // 저장 버튼
        val saveButton = view.findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            AddScheduleInfo.title = title.text.toString()
            AddScheduleInfo.memo = memo.text.toString()
        }

        val knockButton = view.findViewById<TextView>(R.id.knock_button)
        if (AddScheduleInfo.invitersNumber == 0) {
            saveButton.visibility = View.VISIBLE
            knockButton.visibility = View.GONE
        } else {
            saveButton.visibility = View.GONE
            knockButton.visibility = View.VISIBLE
        }
        knockButton.setOnClickListener { }

        val backButton = view.findViewById<View>(R.id.back_button)
        backButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()?.remove(this)?.commit()
            parentFragment?.requireActivity()?.supportFragmentManager
                ?.beginTransaction()?.remove(requireParentFragment())?.commit()
        }


        return view
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
}