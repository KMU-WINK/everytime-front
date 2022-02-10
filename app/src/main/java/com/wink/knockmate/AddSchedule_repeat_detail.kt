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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_repeat_detail, container, false)

        val weeksFrame = view.findViewById<FrameLayout>(R.id.repeat_weeks_frame)
        val monthsFrame = view.findViewById<FrameLayout>(R.id.repeat_month_frame)

        val repeatDay = view.findViewById<EditText>(R.id.repeat_day)
        repeatDay.setText((AddScheduleInfo.getStartCal().get(Calendar.DATE)).toString())

        val radioGroup = view.findViewById<RadioGroup>(R.id.repeat_radioGroup)
        radioGroup.setOnCheckedChangeListener{ group, checkedId->
            when(checkedId){
                R.id.repeat_days_pick->{
                    if(monthsViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, 0f, monthsFrame.height.toFloat())
                        anim.duration = 400
                        anim.fillAfter = true
                        monthsFrame.animation = anim
                        monthsViewBoolean = false

                        monthsFrame.visibility = View.GONE
                    }else if(weeksViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, 0f, weeksFrame.height.toFloat())
                        anim.duration = 400
                        anim.fillAfter = true
                        weeksFrame.animation = anim
                        weeksViewBoolean = false

                        weeksFrame.visibility = View.GONE
                    }
                }
                R.id.repeat_weeks_pick->{
                    if(!weeksViewBoolean && !monthsViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, weeksFrame.height.toFloat(), 0f)
                        anim.duration = 400
                        anim.fillAfter = true
                        weeksFrame.animation = anim
                        weeksViewBoolean = true

                        weeksFrame.visibility = View.VISIBLE
                    }else if(monthsViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, 0f, monthsFrame.height.toFloat())
                        anim.duration = 200
                        anim.fillAfter = true
                        monthsFrame.animation = anim

                        val anim2 = TranslateAnimation(0f, 0f, weeksFrame.height.toFloat(), 0f)
                        anim2.duration = 200
                        anim2.fillAfter = true
                        weeksFrame.animation = anim2
                        weeksViewBoolean = true
                        monthsViewBoolean = false

                        monthsFrame.visibility = View.GONE
                        weeksFrame.visibility = View.VISIBLE
                    }
                }
                R.id.repeat_months_pick->{
                    if(!weeksViewBoolean && !monthsViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, monthsFrame.height.toFloat(), 0f)
                        anim.duration = 400
                        anim.fillAfter = true
                        monthsFrame.animation = anim
                        monthsViewBoolean = true

                        monthsFrame.visibility = View.VISIBLE
                    }else if(weeksViewBoolean){
                        val anim = TranslateAnimation(0f, 0f, 0f, weeksFrame.height.toFloat())
                        anim.duration = 200
                        anim.fillAfter = true
                        weeksFrame.animation = anim

                        val anim2 = TranslateAnimation(0f, 0f, monthsFrame.height.toFloat(), 0f)
                        anim2.duration = 200
                        anim2.fillAfter = true
                        monthsFrame.animation = anim2
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
                AddScheduleInfo.setRepeatInterval(repeatInterval.toString().toInt())
                AddScheduleInfo.setRepeatAllCount(repeatCount.toString().toInt())
                AddScheduleInfo.setRepeatDetailType("Days")
            }else if(weeksViewBoolean){
                val monCheckBox = view.findViewById<CheckBox>(R.id.repeat_monday).isChecked
                val tueCheckBox = view.findViewById<CheckBox>(R.id.repeat_tuesday).isChecked
                val wedCheckBox = view.findViewById<CheckBox>(R.id.repeat_wednesday).isChecked
                val thursCheckBox = view.findViewById<CheckBox>(R.id.repeat_thursday).isChecked
                val friCheckBox = view.findViewById<CheckBox>(R.id.repeat_friday).isChecked
                val satCheckBox = view.findViewById<CheckBox>(R.id.repeat_saturday).isChecked
                val sunCheckBox = view.findViewById<CheckBox>(R.id.repeat_sunday).isChecked
                val repeatDays = arrayOf(monCheckBox, tueCheckBox, wedCheckBox, thursCheckBox,
                                            friCheckBox, satCheckBox, sunCheckBox)
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.setRepeatDays(repeatDays)
                AddScheduleInfo.setRepeatAllCount(repeatCount.toInt())
                AddScheduleInfo.setRepeatDetailType("Weeks")
            }else if(monthsViewBoolean){
                val repeatInterval = view.findViewById<EditText>(R.id.repeat_interval).text.toString()
                val repeatDay = view.findViewById<EditText>(R.id.repeat_day).text.toString()
                val repeatCount = view.findViewById<EditText>(R.id.repeat_count).text.toString()
                AddScheduleInfo.setRepeatDetailType("Months")
                AddScheduleInfo.setRepeatInterval(repeatInterval.toInt())
                AddScheduleInfo.setRepeatAllCount(repeatCount.toInt())
                AddScheduleInfo.setRepeatDay(repeatDay.toInt())
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame,AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        })

        return view
    }
}