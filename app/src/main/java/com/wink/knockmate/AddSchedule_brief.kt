package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.util.*

class AddSchedule_brief : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.addschedule_brief, container, false)

        val brief_setting = view.findViewById<ConstraintLayout>(R.id.brief_dateNTime)

        val startDateText_brief = view.findViewById<TextView>(R.id.brief_date)
        val startTimeText_brief = view.findViewById<TextView>(R.id.brief_start_time)
        val endTimeText_brief = view.findViewById<TextView>(R.id.brief_end_time)
        val startCal = AddScheduleInfo.getStartCal()
        val endCal = AddScheduleInfo.getEndCal()
        val icon = view.findViewById<ImageView>(R.id.icon)

        startDateText_brief.text = (startCal.get(Calendar.MONTH)+1).toString() + "." + startCal.get(
            Calendar.DATE).toString() + " " + dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)) + "요일"
        startTimeText_brief.text = if(startCal.get(Calendar.AM_PM)===0) "오전" + " " + startCal.get(
            Calendar.HOUR).toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
        else "오후" + " " + startCal.get(Calendar.HOUR).toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
        endTimeText_brief.text = if(endCal.get(Calendar.AM_PM)===0) "오전" + " " + endCal.get(Calendar.HOUR).toString() + "시 " + endCal.get(
            Calendar.MINUTE).toString() + "분"
        else "오후" + " " + endCal.get(Calendar.HOUR).toString() + "시 " + endCal.get(Calendar.MINUTE).toString() + "분"

        AddScheduleInfo.setStartDay(dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)))
        AddScheduleInfo.setEndDay(dayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)))

        brief_setting.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                ?.addToBackStack(null)
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.commit()
        }

        val title = view.findViewById<EditText>(R.id.schedule_title)
        var titleClick = false
        title.setOnFocusChangeListener(object:View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus){
                    if (!titleClick || AddScheduleInfo.getTitle() == null){
                        title.text = null
                    }
                    titleClick = true
                }else{
                    if(AddScheduleInfo.getTitle() != null){
                        title.setText("일정 제목")
                    }
                }
            }
        })

        colorSetting(icon)

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

    private fun colorSetting(v: ImageView){
        if(AddScheduleInfo.color == 1){
            Glide.with(this).load(R.drawable.color1).into(v)
        }else if(AddScheduleInfo.color == 2){
            Glide.with(this).load(R.drawable.color2).into(v)
        }else if(AddScheduleInfo.color == 3){
            Glide.with(this).load(R.drawable.color3).into(v)
        }else if(AddScheduleInfo.color == 4){
            Glide.with(this).load(R.drawable.color4).into(v)
        }else if(AddScheduleInfo.color == 5){
            Glide.with(this).load(R.drawable.color5).into(v)
        }else if(AddScheduleInfo.color == 6){
            Glide.with(this).load(R.drawable.color6).into(v)
        }else if(AddScheduleInfo.color == 7){
            Glide.with(this).load(R.drawable.color7).into(v)
        }else if(AddScheduleInfo.color == 8){
            Glide.with(this).load(R.drawable.color8).into(v)
        }else if(AddScheduleInfo.color == 9){
            Glide.with(this).load(R.drawable.color9).into(v)
        }else if(AddScheduleInfo.color == 10){
            Glide.with(this).load(R.drawable.color10).into(v)
        }else if(AddScheduleInfo.color == 11){
            Glide.with(this).load(R.drawable.color11).into(v)
        }else if(AddScheduleInfo.color == 12){
            Glide.with(this).load(R.drawable.color12).into(v)
        }
    }
}