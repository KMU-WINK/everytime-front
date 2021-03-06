package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
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

        val tempType = AddScheduleInfo.repeatType
        val tempDetailType = AddScheduleInfo.repeatDetailType
        val tempRepeatDays = AddScheduleInfo.repeatDays
        val tempRepeatDay = AddScheduleInfo.repeatDay
        val tempRepeatAllCount = AddScheduleInfo.repeatAllCount
        val tempRepeatInterval = AddScheduleInfo.repeatInterval

        val noRepeat = view.findViewById<ConstraintLayout>(R.id.repeat_no)
        val everyDay = view.findViewById<ConstraintLayout>(R.id.repeat_everyday)
        val everyWeek = view.findViewById<ConstraintLayout>(R.id.repeat_everyweek)
        val everyMonth = view.findViewById<ConstraintLayout>(R.id.repeat_everymonth)
        val everyYear = view.findViewById<ConstraintLayout>(R.id.repeat_everyyear)
        val custom = view.findViewById<ConstraintLayout>(R.id.repeat_custom)

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        val saveButton = view.findViewById<TextView>(R.id.repeat_save_button)

        val noRepeatCheck = view.findViewById<CheckBox>(R.id.repeat_no_check)
        val everyDayCheck = view.findViewById<CheckBox>(R.id.repeat_everyday_check)
        val everyWeekCheck = view.findViewById<CheckBox>(R.id.repeat_everyweek_check)
        val everyMonthCheck = view.findViewById<CheckBox>(R.id.repeat_everymonth_check)
        val everyYearCheck = view.findViewById<CheckBox>(R.id.repeat_everyyear_check)
        val customCheck = view.findViewById<CheckBox>(R.id.repeat_custom_check)

        if (AddScheduleInfo.repeatType == "?????? ??????" || AddScheduleInfo.repeatType == "") {
            noRepeatCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "??????") {
            everyDayCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "??????") {
            everyWeekCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "??????") {
            everyMonthCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "??????") {
            everyYearCheck.isChecked = true
        } else {
            customCheck.isChecked = true
        }

        fun initFalse() {
            noRepeatCheck.isChecked = false
            everyDayCheck.isChecked = false
            everyWeekCheck.isChecked = false
            everyMonthCheck.isChecked = false
            everyYearCheck.isChecked = false
            customCheck.isChecked = false
        }

        noRepeat.setOnClickListener {
            AddScheduleInfo.repeatType = "?????? ??????"
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDay.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeek.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonth.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYear.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyYearCheck.isChecked = true
        }
        custom.setOnClickListener {
            AddScheduleInfo.repeatType = "?????? ??????"
            initFalse()
            customCheck.isChecked = true

            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        noRepeatCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "?????? ??????"
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDayCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeekCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonthCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYearCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "??????"
            initFalse()
            everyYearCheck.isChecked = true
        }
        customCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "?????? ??????"
            initFalse()
            customCheck.isChecked = true

            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat_detail())
                ?.addToBackStack(null)
                ?.commit()
        }
        backButton.setOnClickListener {
            AddScheduleInfo.repeatType = tempType
            AddScheduleInfo.repeatDetailType = tempDetailType
            AddScheduleInfo.repeatDay = tempRepeatDay
            AddScheduleInfo.repeatAllCount = tempRepeatAllCount
            AddScheduleInfo.repeatDays = tempRepeatDays
            AddScheduleInfo.repeatInterval = tempRepeatInterval
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