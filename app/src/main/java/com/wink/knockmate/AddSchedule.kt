package com.wink.knockmate

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class AddSchedule : BottomSheetDialogFragment() {
    override fun getTheme(): Int =R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState)

        val view = View.inflate(context, R.layout.addschedule, null)
//        view.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        bottomSheet.setContentView(view)

//        val transition = Fade()
//        transition.setDuration(200)

//        val frame = view.findViewById<FrameLayout>(R.id.addschedule_frame)
//        val brief = view.findViewById<FrameLayout>(R.id.brief_frame)
//        val detail = view.findViewById<FrameLayout>(R.id.detail_frame)
//        val brief_setting = view.findViewById<ConstraintLayout>(R.id.brief_dateNTime)
////        val space = view.findViewById<View>(R.id.space)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

//        val mainScheduleFrame = view.findViewById<ConstraintLayout>(R.id.main_schedule_frame)
//
//        brief_setting.setOnClickListener(View.OnClickListener {
//            transition.addTarget(R.id.detail_frame)
//            TransitionManager.beginDelayedTransition(brief, transition)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }) // brief setting 누르고 state를 STATE_EXPANDED로 바꾸는데 자연스럽게 바뀌게 하기

//        bottomSheetBehavior.peekHeight = 400

        childFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.addschedule_frame, AddSchedule_brief())
            .commit()

        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(BottomSheetBehavior.STATE_COLLAPSED == newState){
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.addschedule_frame, AddSchedule_brief())
                        .commit()

//                    brief.visibility = View.VISIBLE
//                    frame.visibility = View.INVISIBLE
//                    detail.visibility = View.INVISIBLE
////                    space.visibility = View.INVISIBLE
                }
                if(BottomSheetBehavior.STATE_EXPANDED == newState){
                    childFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.addschedule_frame, AddSchedule_detail())
                        .commit()
//                    brief.visibility = View.INVISIBLE
//                    detail.visibility = View.VISIBLE
////                    space.visibility = View.VISIBLE
                }
                if(BottomSheetBehavior.STATE_DRAGGING == newState){

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })



//        // detail, brief text 설정
//        val startDateText = view.findViewById<TextView>(R.id.detail_start_date)
//        val startTimeText = view.findViewById<TextView>(R.id.detail_start_time)
//        val endDateText = view.findViewById<TextView>(R.id.detail_end_date)
//        val endTimeText = view.findViewById<TextView>(R.id.detail_end_time)
//        val startDateText_brief = view.findViewById<TextView>(R.id.brief_date)
//        val startTimeText_brief = view.findViewById<TextView>(R.id.brief_start_time)
//        val endTimeText_brief = view.findViewById<TextView>(R.id.brief_end_time)
//        val startCal = AddScheduleInfo.getStartCal()
//        val endCal = AddScheduleInfo.getEndCal()
//
//        startDateText.text = (startCal.get(Calendar.MONTH)+1).toString() + "월" + startCal.get(Calendar.DATE).toString() + "일 (" + dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)) + ")"
//        startTimeText.text = if(startCal.get(Calendar.AM_PM)===0) "오전" + " " + startCal.get(Calendar.HOUR).toString() + ":" + startCal.get(Calendar.MINUTE).toString()
//            else "오후" + " " + startCal.get(Calendar.HOUR).toString() + ":" + startCal.get(Calendar.MINUTE).toString()
//        endDateText.text = (endCal.get(Calendar.MONTH)+1).toString() + "월" + endCal.get(Calendar.DATE).toString() + "일 (" + dayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)) + ")"
//        endTimeText.text = if(startCal.get(Calendar.AM_PM)===0) "오전" + " " + endCal.get(Calendar.HOUR).toString() + ":" + endCal.get(Calendar.MINUTE).toString()
//            else "오후" + " " + endCal.get(Calendar.HOUR).toString() + ":" + endCal.get(Calendar.MINUTE).toString()
//
//        startDateText_brief.text = (startCal.get(Calendar.MONTH)+1).toString() + "." + startCal.get(Calendar.DATE).toString() + " " + dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)) + "요일"
//        startTimeText_brief.text = if(startCal.get(Calendar.AM_PM)===0) "오전" + " " + startCal.get(Calendar.HOUR).toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
//            else "오후" + " " + startCal.get(Calendar.HOUR).toString() + "시 " + startCal.get(Calendar.MINUTE).toString() + "분"
//        endTimeText_brief.text = if(endCal.get(Calendar.AM_PM)===0) "오전" + " " + endCal.get(Calendar.HOUR).toString() + "시 " + endCal.get(Calendar.MINUTE).toString() + "분"
//            else "오후" + " " + endCal.get(Calendar.HOUR).toString() + "시 " + endCal.get(Calendar.MINUTE).toString() + "분"
//
//        AddScheduleInfo.setStartDay(dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)))
//        AddScheduleInfo.setEndDay(dayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)))
//
//
//
//        // startpicker
//        val startView = view.findViewById<LinearLayout>(R.id.addschedule_start)
//        val startPicker = view.findViewById<ConstraintLayout>(R.id.start_picker)
//        val startDatePicker = view.findViewById<NumberPicker>(R.id.start_date_picker)
//        val startAmOrPmPicker = view.findViewById<NumberPicker>(R.id.start_am_or_pm)
//        val startTimePicker = view.findViewById<NumberPicker>(R.id.start_time_picker)
//        val month_list = arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
//        val day_list1 = arrayOf("1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일", "31일")
//        val day_list2 = arrayOf("1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일", "30일")
//        val day_list3 = arrayOf("1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일")
//        val day_list4 = arrayOf("1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일", "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일", "24일", "25일", "26일", "27일", "28일", "29일")
//        val temp = mutableListOf<String>()
//        for (i in month_list){
//            if(i === "1월" || i === "3월" || i === "5월" || i === "7월" || i === "8월" || i === "10월" || i === "12월"){
//                for(j in day_list1)
//                    temp.add(i+j)
//            }else if(i === "2월"){
//                for(j in day_list3)
//                    temp.add(i+j)
//            }else
//                for(j in day_list2)
//                    temp.add(i+j)
//        }
//        val hourAm = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11)
//        val hourPm = arrayOf(12,1,2,3,4,5,6,7,8,9,10,11)
//        val minute = arrayOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
//        val temp2 = mutableListOf<String>()
//        for(i in hourAm){
//            for(j in minute)
//                temp2.add(i.toString() + ":" + j.toString())
//        }
//        for(i in hourPm){
//            for(j in minute)
//                temp2.add(i.toString() + ":" + j.toString())
//        }
//        // 윤년 케이스 추가해야함
//        startDatePicker.displayedValues = temp.toTypedArray()
//        startAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
//        startTimePicker.displayedValues = temp2.toTypedArray()
//        startDatePicker.value = (startCal.get(Calendar.MONTH)+1).toString().toInt()
//        if(startCal.get(Calendar.AM_PM) == Calendar.AM){
//            startAmOrPmPicker.value = 0
//        }else{ startAmOrPmPicker.value = 1 }
//
//        val endPicker = view.findViewById<ConstraintLayout>(R.id.end_picker)
//
//        startView.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.VISIBLE
//            endPicker.visibility = View.GONE
//        })
//
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
//        endDatePicker.value = (endCal.get(Calendar.MONTH)+1).toString().toInt()
//        if(endCal.get(Calendar.AM_PM) == Calendar.AM){
//            endAmOrPmPicker.value = 0
//        }else{ endAmOrPmPicker.value = 1 }
//
//        endView.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.VISIBLE
//        })
//
//
//
//        // 일정 제목
//        val title = view.findViewById<EditText>(R.id.schedule_title)
//        var titleClick = false
//        title.setOnFocusChangeListener(object:View.OnFocusChangeListener{
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                if(hasFocus){
//                    if (!titleClick || AddScheduleInfo.getTitle() == null){
//                        title.text = null
//                    }
//                    titleClick = true
//                    startPicker.visibility = View.GONE
//                    endPicker.visibility = View.GONE
//                }else{
//                    if(AddScheduleInfo.getTitle() != null){
//                        title.setText("일정 제목")
//                    }
//                }
//            }
//        })
//        title.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//            override fun afterTextChanged(s: Editable?) {
//                AddScheduleInfo.setTitle(title.text.toString())
//            }
//        })
//
//
//
//
//        // 일정 메모
//        val memo = view.findViewById<EditText>(R.id.memo)
//        memo.setOnFocusChangeListener(object:View.OnFocusChangeListener{
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                if(hasFocus){
//                    startPicker.visibility = View.GONE
//                    endPicker.visibility = View.GONE
//                }
//            }
//
//        })
//        memo.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//            override fun afterTextChanged(s: Editable?) {
//                AddScheduleInfo.setMemo(memo.text.toString())
//            }
//        })
//
//
//
//        // 참석자초대로 넘어가는 버튼
//        val inviters = view.findViewById<TextView>(R.id.inviters)
//        if(AddScheduleInfo.invitersNumber == 0){
//            inviters.text = ""
//        }else if(AddScheduleInfo.invitersNumber == 1){
//            inviters.text = AddScheduleInfo.inviteMembers[0].nickname
//        }else{
//            inviters.text = AddScheduleInfo.inviteMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber-1).toString() + "명"
//        }
//        val to_invite_button = view.findViewById<ConstraintLayout>(R.id.to_invite_button)
//        to_invite_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            frame.visibility = View.VISIBLE
//            childFragmentManager.beginTransaction().add(R.id.addschedule_frame, AddSchedule_invite()).addToBackStack(null).commit()
//        })
//
//
//
//        // 반복 일정으로 넘어가는 버튼
//        val repeatText = view.findViewById<TextView>(R.id.repeat_text)
//        repeatText.text = AddScheduleInfo.getRepeatType()
//        val to_repeat_button = view.findViewById<ConstraintLayout>(R.id.to_repeat_button)
//        to_repeat_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            frame.visibility = View.VISIBLE
//            childFragmentManager
//                .beginTransaction()
//                .replace(R.id.addschedule_frame, AddSchedule_repeat())
//                .commit()
//        })
//
//
//
//        // 일정 색상 선택으로 넘어가는 버튼
//        val to_colorPick_button = view.findViewById<ConstraintLayout>(R.id.to_colorPick_button)
//        to_colorPick_button.setOnClickListener(View.OnClickListener {
//            startPicker.visibility = View.GONE
//            endPicker.visibility = View.GONE
//            frame.visibility = View.VISIBLE
//            childFragmentManager
//                .beginTransaction()
//                .replace(R.id.addschedule_frame, AddSchedule_colorPick())
//                .commit()
//        })
//
//
//
//        // 저장 버튼
//        val saveButton = view.findViewById<TextView>(R.id.save_button)
//        saveButton.setOnClickListener {
//            dismiss()
//        }


        return bottomSheet
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