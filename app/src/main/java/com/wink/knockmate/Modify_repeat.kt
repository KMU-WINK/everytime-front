package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Modify_repeat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addschedule_repeat)

        val tempType = AddScheduleInfo.repeatType
        val tempDetailType = AddScheduleInfo.repeatDetailType
        val tempRepeatDays = AddScheduleInfo.repeatDays
        val tempRepeatDay = AddScheduleInfo.repeatDay
        val tempRepeatAllCount = AddScheduleInfo.repeatAllCount
        val tempRepeatInterval = AddScheduleInfo.repeatInterval

        val noRepeat = findViewById<ConstraintLayout>(R.id.repeat_no)
        val everyDay = findViewById<ConstraintLayout>(R.id.repeat_everyday)
        val everyWeek = findViewById<ConstraintLayout>(R.id.repeat_everyweek)
        val everyMonth = findViewById<ConstraintLayout>(R.id.repeat_everymonth)
        val everyYear = findViewById<ConstraintLayout>(R.id.repeat_everyyear)
        val custom = findViewById<ConstraintLayout>(R.id.repeat_custom)

        val backButton = findViewById<View>(R.id.back_button)
        val saveButton = findViewById<TextView>(R.id.repeat_save_button)

        val noRepeatCheck = findViewById<CheckBox>(R.id.repeat_no_check)
        val everyDayCheck = findViewById<CheckBox>(R.id.repeat_everyday_check)
        val everyWeekCheck = findViewById<CheckBox>(R.id.repeat_everyweek_check)
        val everyMonthCheck = findViewById<CheckBox>(R.id.repeat_everymonth_check)
        val everyYearCheck = findViewById<CheckBox>(R.id.repeat_everyyear_check)
        val customCheck = findViewById<CheckBox>(R.id.repeat_custom_check)

        if (AddScheduleInfo.repeatType == "반복 안함" || AddScheduleInfo.repeatType == "") {
            noRepeatCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "매일") {
            everyDayCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "매주") {
            everyWeekCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "매월") {
            everyMonthCheck.isChecked = true
        } else if (AddScheduleInfo.repeatType == "매년") {
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
            AddScheduleInfo.repeatType = "반복 안함"
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDay.setOnClickListener {
            AddScheduleInfo.repeatType = "매일"
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeek.setOnClickListener {
            AddScheduleInfo.repeatType = "매주"
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonth.setOnClickListener {
            AddScheduleInfo.repeatType = "매월"
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYear.setOnClickListener {
            AddScheduleInfo.repeatType = "매년"
            initFalse()
            everyYearCheck.isChecked = true
        }
        custom.setOnClickListener {
            AddScheduleInfo.repeatType = "맞춤 설정"
            initFalse()
            customCheck.isChecked = true

            val intent = Intent(this, Modify_repeat_detail::class.java)
            startActivity(intent)
            finish()
        }
        noRepeatCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "반복 안함"
            initFalse()
            noRepeatCheck.isChecked = true
        }
        everyDayCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "매일"
            initFalse()
            everyDayCheck.isChecked = true
        }
        everyWeekCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "매주"
            initFalse()
            everyWeekCheck.isChecked = true
        }
        everyMonthCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "매월"
            initFalse()
            everyMonthCheck.isChecked = true
        }
        everyYearCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "매년"
            initFalse()
            everyYearCheck.isChecked = true
        }
        customCheck.setOnClickListener {
            AddScheduleInfo.repeatType = "맞춤 설정"
            initFalse()
            customCheck.isChecked = true

            val intent = Intent(this, Modify_repeat_detail::class.java)
            startActivity(intent)
            finish()
        }
        backButton.setOnClickListener {
            AddScheduleInfo.repeatType = tempType
            AddScheduleInfo.repeatDetailType = tempDetailType
            AddScheduleInfo.repeatDay = tempRepeatDay
            AddScheduleInfo.repeatAllCount = tempRepeatAllCount
            AddScheduleInfo.repeatDays = tempRepeatDays
            AddScheduleInfo.repeatInterval = tempRepeatInterval
            val intent = Intent(this, ModifyScheduleActivity::class.java)
            startActivity(intent)
            finish()
        }
        saveButton.setOnClickListener {
            val intent = Intent(this, ModifyScheduleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}