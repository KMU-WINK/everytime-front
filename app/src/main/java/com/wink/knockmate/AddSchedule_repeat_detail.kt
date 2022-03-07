package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.util.*

class AddSchedule_repeat_detail : Fragment() {
    private var weeksViewBoolean: Boolean = false
    private var monthsViewBoolean: Boolean = false
    private var weekInitBoolean: Boolean = false

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_repeat_detail, container, false)

        val weeksFrame = view.findViewById<FrameLayout>(R.id.repeat_weeks_frame)
        val monthsFrame = view.findViewById<FrameLayout>(R.id.repeat_month_frame)

        var boolean = false

        val tempRepeatType = AddScheduleInfo.repeatDetailType
        val tempRepeatInterval = AddScheduleInfo.repeatInterval
        val tempRepeatAllCount = AddScheduleInfo.repeatAllCount
        val tempRepeatDays = mutableListOf<Boolean>()
        tempRepeatDays.addAll(AddScheduleInfo.repeatDays)
        val tempRepeatDay = AddScheduleInfo.repeatDay
        for (i in 0 until AddScheduleInfo.repeatDays.size) {
            if (AddScheduleInfo.repeatDays[i]) {
                boolean = true
            }
        }


        val weekDay = AddScheduleInfo.startDay

        val repeatMonday = view.findViewById<CheckBox>(R.id.repeat_monday)
        val repeatTuesday = view.findViewById<CheckBox>(R.id.repeat_tuesday)
        val repeatWednesday = view.findViewById<CheckBox>(R.id.repeat_wednesday)
        val repeatThursday = view.findViewById<CheckBox>(R.id.repeat_thursday)
        val repeatFriday = view.findViewById<CheckBox>(R.id.repeat_friday)
        val repeatSaturday = view.findViewById<CheckBox>(R.id.repeat_saturday)
        val repeatSunday = view.findViewById<CheckBox>(R.id.repeat_sunday)

        val repeatInterval = view.findViewById<EditText>(R.id.repeat_interval)
        val repeatCount = view.findViewById<EditText>(R.id.repeat_count)

        val repeatDay = view.findViewById<EditText>(R.id.repeat_day)

        if (!weekInitBoolean) {
            if (weekDay == "월") {
                repeatMonday.isChecked = true
            } else if (weekDay == "화") {
                repeatTuesday.isChecked = true
            } else if (weekDay == "수") {
                repeatWednesday.isChecked = true
            } else if (weekDay == "목") {
                repeatThursday.isChecked = true
            } else if (weekDay == "금") {
                repeatFriday.isChecked = true
            } else if (weekDay == "토") {
                repeatSaturday.isChecked = true
            } else if (weekDay == "일") {
                repeatSunday.isChecked = true
            }
        }

        repeatInterval.setText(AddScheduleInfo.repeatInterval.toString())
        repeatCount.setText(AddScheduleInfo.repeatAllCount.toString())

        if (AddScheduleInfo.repeatDetailType == "Days") {
            monthsViewBoolean = false
            monthsFrame.visibility = View.GONE
            weeksViewBoolean = false
            weeksFrame.visibility = View.GONE
            view.findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = true
            view.findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = false
            view.findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = false
            repeatDay.setText((AddScheduleInfo.startCal.get(Calendar.DATE)).toString())
            if (weekDay == "월") {
                repeatMonday.isChecked = true
            } else if (weekDay == "화") {
                repeatTuesday.isChecked = true
            } else if (weekDay == "수") {
                repeatWednesday.isChecked = true
            } else if (weekDay == "목") {
                repeatThursday.isChecked = true
            } else if (weekDay == "금") {
                repeatFriday.isChecked = true
            } else if (weekDay == "토") {
                repeatSaturday.isChecked = true
            } else if (weekDay == "일") {
                repeatSunday.isChecked = true
            }
            weekInitBoolean = false
        } else if (AddScheduleInfo.repeatDetailType == "Weeks") {
            weeksViewBoolean = true
            monthsViewBoolean = false
            monthsFrame.visibility = View.GONE
            weeksFrame.visibility = View.VISIBLE
            view.findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = false
            view.findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = true
            view.findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = false
            repeatDay.setText((AddScheduleInfo.startCal.get(Calendar.DATE)).toString())
            for (i in 0 until AddScheduleInfo.repeatDays.size) {
                if (AddScheduleInfo.repeatDays[i]) {
                    weekInitBoolean = AddScheduleInfo.repeatDays[i]
                    if (i == 0) {
                        repeatMonday.isChecked = true
                    } else if (i == 1) {
                        repeatTuesday.isChecked = true
                    } else if (i == 2) {
                        repeatWednesday.isChecked = true
                    } else if (i == 3) {
                        repeatThursday.isChecked = true
                    } else if (i == 4) {
                        repeatFriday.isChecked = true
                    } else if (i == 5) {
                        repeatSaturday.isChecked = true
                    } else if (i == 6) {
                        repeatSunday.isChecked = true
                    }
                }
            }
        } else {
            monthsViewBoolean = true
            weeksViewBoolean = false
            weeksFrame.visibility = View.GONE
            monthsFrame.visibility = View.VISIBLE
            view.findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = false
            view.findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = false
            view.findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = true
            weekInitBoolean = false
            if (weekDay == "월") {
                repeatMonday.isChecked = true
            } else if (weekDay == "화") {
                repeatTuesday.isChecked = true
            } else if (weekDay == "수") {
                repeatWednesday.isChecked = true
            } else if (weekDay == "목") {
                repeatThursday.isChecked = true
            } else if (weekDay == "금") {
                repeatFriday.isChecked = true
            } else if (weekDay == "토") {
                repeatSaturday.isChecked = true
            } else if (weekDay == "일") {
                repeatSunday.isChecked = true
            }
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.repeat_radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.repeat_days_pick -> {
                    if (monthsViewBoolean) {
                        monthsViewBoolean = false
                        monthsFrame.visibility = View.GONE
                    } else if (weeksViewBoolean) {
                        weeksViewBoolean = false
                        weeksFrame.visibility = View.GONE
                    }
                }
                R.id.repeat_weeks_pick -> {
                    if (!weeksViewBoolean && !monthsViewBoolean) {
                        weeksViewBoolean = true
                        weeksFrame.visibility = View.VISIBLE
                    } else if (monthsViewBoolean) {
                        weeksViewBoolean = true
                        monthsViewBoolean = false
                        monthsFrame.visibility = View.GONE
                        weeksFrame.visibility = View.VISIBLE
                    }
                }
                R.id.repeat_months_pick -> {
                    if (!weeksViewBoolean && !monthsViewBoolean) {
                        monthsViewBoolean = true
                        monthsFrame.visibility = View.VISIBLE
                    } else if (weeksViewBoolean) {
                        monthsViewBoolean = true
                        weeksViewBoolean = false
                        weeksFrame.visibility = View.GONE
                        monthsFrame.visibility = View.VISIBLE
                    }
                }

            }
        }

        val repeatSaveButton = view.findViewById<TextView>(R.id.repeat_save_button)

        repeatSaveButton.setOnClickListener(View.OnClickListener {
            if (!weeksViewBoolean && !monthsViewBoolean) {
                AddScheduleInfo.repeatInterval = repeatInterval.text.toString().toInt()
                AddScheduleInfo.repeatAllCount = repeatCount.text.toString().toInt()
                AddScheduleInfo.repeatDetailType = "Days"
            } else if (weeksViewBoolean) {
                val repeatDays = mutableListOf(
                    repeatMonday.isChecked, repeatTuesday.isChecked, repeatWednesday.isChecked,
                    repeatThursday.isChecked, repeatFriday.isChecked, repeatSaturday.isChecked,
                    repeatSunday.isChecked
                )
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDays = repeatDays
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDetailType = "Weeks"
            } else if (monthsViewBoolean) {
                val repeatInterval =
                    view.findViewById<EditText>(R.id.repeat_interval).text.toString()
                val repeatDay = view.findViewById<EditText>(R.id.repeat_day).text.toString()
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDetailType = "Months"
                AddScheduleInfo.repeatInterval = repeatInterval.toInt()
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDay = repeatDay.toInt()
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        })

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            if (boolean) {
                AddScheduleInfo.repeatDetailType = tempRepeatType
                AddScheduleInfo.repeatDay = tempRepeatDay
                AddScheduleInfo.repeatDays = tempRepeatDays
                AddScheduleInfo.repeatAllCount = tempRepeatAllCount
                AddScheduleInfo.repeatInterval = tempRepeatInterval
            } else {
                AddScheduleInfo.repeatDetailType = tempRepeatType
                AddScheduleInfo.repeatType = "반복 안함"
                AddScheduleInfo.repeatDay = tempRepeatDay
                AddScheduleInfo.repeatDays = tempRepeatDays
                AddScheduleInfo.repeatAllCount = tempRepeatAllCount
                AddScheduleInfo.repeatInterval = tempRepeatInterval
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_repeat())
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }
}