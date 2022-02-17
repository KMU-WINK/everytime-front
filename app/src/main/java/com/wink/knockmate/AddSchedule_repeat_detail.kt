package com.wink.knockmate

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
    private var weeksViewBoolean : Boolean = false
    private var monthsViewBoolean : Boolean = false
    private var weekInitBoolean : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_repeat_detail, container, false)

        val weeksFrame = view.findViewById<FrameLayout>(R.id.repeat_weeks_frame)
        val monthsFrame = view.findViewById<FrameLayout>(R.id.repeat_month_frame)

        val weekDay = AddScheduleInfo.startDay

        val repeatMonday = view.findViewById<CheckBox>(R.id.repeat_monday)
        val repeatTuesday = view.findViewById<CheckBox>(R.id.repeat_tuesday)
        val repeatWednesday = view.findViewById<CheckBox>(R.id.repeat_wednesday)
        val repeatThursday = view.findViewById<CheckBox>(R.id.repeat_thursday)
        val repeatFriday = view.findViewById<CheckBox>(R.id.repeat_friday)
        val repeatSaturday = view.findViewById<CheckBox>(R.id.repeat_saturday)
        val repeatSunday = view.findViewById<CheckBox>(R.id.repeat_sunday)

        val repeatDay = view.findViewById<EditText>(R.id.repeat_day)
        repeatDay.setText((AddScheduleInfo.startCal.get(Calendar.DATE)).toString())

        for(i in 0 until AddScheduleInfo.repeatDays.size){
            if(AddScheduleInfo.repeatDays[i]){
                weekInitBoolean = AddScheduleInfo.repeatDays[i]
                if(i==0){
                    repeatMonday.isChecked = true
                }else if(i==1){
                    repeatTuesday.isChecked = true
                }else if(i==2){
                    repeatWednesday.isChecked = true
                }else if(i==3){
                    repeatThursday.isChecked = true
                }else if(i==4){
                    repeatFriday.isChecked = true
                }else if(i==5){
                    repeatSaturday.isChecked = true
                }else if(i==6){
                    repeatSunday.isChecked = true
                }
            }
        }

        if(!weekInitBoolean){
            if(weekDay == "월"){
                repeatMonday.isChecked = true
            }else if(weekDay == "화"){
                repeatTuesday.isChecked = true
            }else if(weekDay == "수"){
                repeatWednesday.isChecked = true
            }else if(weekDay == "목"){
                repeatThursday.isChecked = true
            }else if(weekDay == "금"){
                repeatFriday.isChecked = true
            }else if(weekDay == "토"){
                repeatSaturday.isChecked = true
            }else if(weekDay == "일"){
                repeatSunday.isChecked = true
            }
        }

        val radioGroup = view.findViewById<RadioGroup>(R.id.repeat_radioGroup)
        radioGroup.setOnCheckedChangeListener{ group, checkedId->
            when(checkedId){
                R.id.repeat_days_pick->{
                    if(monthsViewBoolean){
                        monthsViewBoolean = false
                        monthsFrame.visibility = View.GONE
                    }else if(weeksViewBoolean){
                        weeksViewBoolean = false
                        weeksFrame.visibility = View.GONE
                    }
                }
                R.id.repeat_weeks_pick->{
                    if(!weeksViewBoolean && !monthsViewBoolean){
                        weeksViewBoolean = true
                        weeksFrame.visibility = View.VISIBLE
                    }else if(monthsViewBoolean){
                        weeksViewBoolean = true
                        monthsViewBoolean = false
                        monthsFrame.visibility = View.GONE
                        weeksFrame.visibility = View.VISIBLE
                    }
                }
                R.id.repeat_months_pick->{
                    if(!weeksViewBoolean && !monthsViewBoolean){
                        monthsViewBoolean = true
                        monthsFrame.visibility = View.VISIBLE
                    }else if(weeksViewBoolean){
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
            if(!weeksViewBoolean&&!monthsViewBoolean){
                val repeatInterval = view.findViewById<EditText>(R.id.repeat_interval).text
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text
                AddScheduleInfo.repeatInterval = repeatInterval.toString().toInt()
                AddScheduleInfo.repeatAllCount = repeatCount.toString().toInt()
                AddScheduleInfo.repeatDetailType = "Days"
            }else if(weeksViewBoolean){
                val monCheckBox = view.findViewById<CheckBox>(R.id.repeat_monday).isChecked
                val tueCheckBox = view.findViewById<CheckBox>(R.id.repeat_tuesday).isChecked
                val wedCheckBox = view.findViewById<CheckBox>(R.id.repeat_wednesday).isChecked
                val thursCheckBox = view.findViewById<CheckBox>(R.id.repeat_thursday).isChecked
                val friCheckBox = view.findViewById<CheckBox>(R.id.repeat_friday).isChecked
                val satCheckBox = view.findViewById<CheckBox>(R.id.repeat_saturday).isChecked
                val sunCheckBox = view.findViewById<CheckBox>(R.id.repeat_sunday).isChecked
                val repeatDays = mutableListOf(monCheckBox, tueCheckBox, wedCheckBox, thursCheckBox,
                                            friCheckBox, satCheckBox, sunCheckBox)
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDays = repeatDays
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDetailType = "Weeks"
            }else if(monthsViewBoolean){
                val repeatInterval = view.findViewById<EditText>(R.id.repeat_interval).text.toString()
                val repeatDay = view.findViewById<EditText>(R.id.repeat_day).text.toString()
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.repeatDetailType = "Months"
                AddScheduleInfo.repeatInterval = repeatInterval.toInt()
                AddScheduleInfo.repeatAllCount = repeatCount.toInt()
                AddScheduleInfo.repeatDay = repeatDay.toInt()
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame,AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        })

        return view
    }

    private fun dayOfWeek(d:Int) : String{
        return when(d){
            1-> "일"
            2-> "월"
            3-> "화"
            4-> "수"
            5-> "목"
            6-> "금"
            7-> "토"
            else -> " "
        }
    }
}