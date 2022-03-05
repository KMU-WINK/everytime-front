package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Modify_repeat_detail : AppCompatActivity() {
    private var weeksViewBoolean: Boolean = false
    private var monthsViewBoolean: Boolean = false
    private var weekInitBoolean: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_repeat_detail)

        val weeksFrame = findViewById<FrameLayout>(R.id.repeat_weeks_frame)
        val monthsFrame = findViewById<FrameLayout>(R.id.repeat_month_frame)

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

        val repeatMonday = findViewById<CheckBox>(R.id.repeat_monday)
        val repeatTuesday = findViewById<CheckBox>(R.id.repeat_tuesday)
        val repeatWednesday = findViewById<CheckBox>(R.id.repeat_wednesday)
        val repeatThursday = findViewById<CheckBox>(R.id.repeat_thursday)
        val repeatFriday = findViewById<CheckBox>(R.id.repeat_friday)
        val repeatSaturday = findViewById<CheckBox>(R.id.repeat_saturday)
        val repeatSunday = findViewById<CheckBox>(R.id.repeat_sunday)

        val repeatInterval = findViewById<EditText>(R.id.repeat_interval)
        val repeatCount = findViewById<EditText>(R.id.repeat_count)

        val repeatDay = findViewById<EditText>(R.id.repeat_day)

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
            findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = true
            findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = false
            findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = false
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
            findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = false
            findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = true
            findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = false
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
            findViewById<RadioButton>(R.id.repeat_days_pick).isChecked = false
            findViewById<RadioButton>(R.id.repeat_weeks_pick).isChecked = false
            findViewById<RadioButton>(R.id.repeat_months_pick).isChecked = true
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

        val radioGroup = findViewById<RadioGroup>(R.id.repeat_radioGroup)
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

        val repeatSaveButton = findViewById<TextView>(R.id.repeat_save_button)

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
                val repeatCount = findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDays = repeatDays
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDetailType = "Weeks"
            } else if (monthsViewBoolean) {
                val repeatInterval =
                    findViewById<EditText>(R.id.repeat_interval).text.toString()
                val repeatDay = findViewById<EditText>(R.id.repeat_day).text.toString()
                val repeatCount = findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDetailType = "Months"
                AddScheduleInfo.repeatInterval = repeatInterval.toInt()
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDay = repeatDay.toInt()
            }

            val intent = Intent(this, Modify_repeat::class.java)
            startActivity(intent)
            finish()
        })

        val backButton = findViewById<View>(R.id.back_button)
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
            val intent = Intent(this, Modify_repeat::class.java)
            startActivity(intent)
            finish()
        }
    }
}