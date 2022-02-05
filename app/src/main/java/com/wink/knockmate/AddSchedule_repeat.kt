package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class AddSchedule_repeat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.addschedule_repeat, container, false)

        val tempType = AddScheduleInfo.getRepeatType()
        val tempDetailType = AddScheduleInfo.getRepeatDetailType()
        val tempRepeatDays = AddScheduleInfo.getRepeatDays()
        val tempRepeatDay = AddScheduleInfo.getRepeatDay()
        val tempRepeatAllCount = AddScheduleInfo.getRepeatAllCount()
        val tempRepeatInterval = AddScheduleInfo.getRepeatInterval()

        val noRepeat = view.findViewById<ConstraintLayout>(R.id.repeat_no)
        val everyDay = view.findViewById<ConstraintLayout>(R.id.repeat_everyday)
        val everyWeek = view.findViewById<ConstraintLayout>(R.id.repeat_everyweek)
        val everyMonth = view.findViewById<ConstraintLayout>(R.id.repeat_everymonth)
        val everyYear = view.findViewById<ConstraintLayout>(R.id.repeat_everyyear)
        val custom = view.findViewById<ConstraintLayout>(R.id.repeat_custom)

        val backButton = view.findViewById<View>(R.id.back_button)
        val saveButton = view.findViewById<TextView>(R.id.repeat_save_button)

        val noRepeatCheck = view.findViewById<CheckBox>(R.id.repeat_no_check)
        val everyDayCheck = view.findViewById<CheckBox>(R.id.repeat_everyday_check)
        val everyWeekCheck = view.findViewById<CheckBox>(R.id.repeat_everyweek_check)
        val everyMonthCheck = view.findViewById<CheckBox>(R.id.repeat_everymonth_check)
        val everyYearCheck = view.findViewById<CheckBox>(R.id.repeat_everyyear_check)
        val customCheck = view.findViewById<CheckBox>(R.id.repeat_custom_check)

        if(AddScheduleInfo.getRepeatType() == "반복 안함" || AddScheduleInfo.getRepeatType() == ""){
            noRepeatCheck.isChecked = true
        }else if(AddScheduleInfo.getRepeatType() == "매일"){
            everyDayCheck.isChecked = true
        }else if(AddScheduleInfo.getRepeatType() == "매주"){
            everyWeekCheck.isChecked = true
        }else if(AddScheduleInfo.getRepeatType() == "매월"){
            everyMonthCheck.isChecked = true
        }else if(AddScheduleInfo.getRepeatType() == "매년"){
            everyYearCheck.isChecked = true
        }else{
            customCheck.isChecked = true
        }

        fun initFalse(){
            noRepeatCheck.isChecked = false
            everyDayCheck.isChecked = false
            everyWeekCheck.isChecked = false
            everyMonthCheck.isChecked = false
            everyYearCheck.isChecked = false
            customCheck.isChecked = false
        }

        noRepeat.setOnClickListener {
            AddScheduleInfo.setRepeatType("반복 안함")
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDay.setOnClickListener {
            AddScheduleInfo.setRepeatType("매일")
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeek.setOnClickListener {
            AddScheduleInfo.setRepeatType("매주")
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonth.setOnClickListener {
            AddScheduleInfo.setRepeatType("매월")
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYear.setOnClickListener {
            AddScheduleInfo.setRepeatType("매년")
            initFalse()
            everyYearCheck.isChecked = true
        }
        custom.setOnClickListener {
            AddScheduleInfo.setRepeatType("맞춤 설정")
            initFalse()
            customCheck.isChecked = true

            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        noRepeatCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("반복 안함")
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDayCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("매일")
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeekCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("매주")
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonthCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("매월")
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYearCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("매년")
            initFalse()
            everyYearCheck.isChecked = true
        }
        customCheck.setOnClickListener {
            AddScheduleInfo.setRepeatType("맞춤 설정")
            initFalse()
            customCheck.isChecked = true

            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        backButton.setOnClickListener {
            AddScheduleInfo.setRepeatType(tempType)
            AddScheduleInfo.setRepeatDetailType(tempDetailType)
            AddScheduleInfo.setRepeatDay(tempRepeatDay)
            AddScheduleInfo.setRepeatAllCount(tempRepeatAllCount)
            AddScheduleInfo.setRepeatDays(tempRepeatDays)
            AddScheduleInfo.setRepeatInterval(tempRepeatInterval)
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        saveButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

}