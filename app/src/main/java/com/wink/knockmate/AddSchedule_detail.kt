package com.wink.knockmate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.*

class AddSchedule_detail : Fragment() {
    private var saveState = false
    private var startpicker = false
    private var endpicker = false

    @RequiresApi(Build.VERSION_CODES.O)
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

        startDateText.text = (startCal.get(Calendar.MONTH) + 1).toString() + "월 " + startCal.get(
            Calendar.DATE
        ).toString() + "일 "
        startTimeText.text =
            if (startCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                startCal.get(Calendar.MINUTE)
            )
            else "오후" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                startCal.get(
                    Calendar.MINUTE
                )
            )

        endDateText.text =
            (endCal.get(Calendar.MONTH) + 1).toString() + "월 " + endCal.get(Calendar.DATE)
                .toString() + "일"
        endTimeText.text =
            if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(Calendar.MINUTE)
            )
            else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(
                    Calendar.MINUTE
                )
            )

        val memo = view.findViewById<EditText>(R.id.memo)
        val title = view.findViewById<EditText>(R.id.schedule_title)

        //startpicker
        val startView = view.findViewById<LinearLayout>(R.id.addschedule_start)
        val endView = view.findViewById<LinearLayout>(R.id.addschedule_end)
        val startDatePicker = view.findViewById<DatePicker>(R.id.start_picker_date)
        val startTimePicker = view.findViewById<TimePicker>(R.id.start_picker_time)
        val endDatePicker = view.findViewById<DatePicker>(R.id.end_picker_date)
        val endTimePicker = view.findViewById<TimePicker>(R.id.end_picker_time)
        startView.setOnClickListener {
            if (!startpicker) {
                startpicker = true

                startDatePicker.isVisible = true
                startTimePicker.isVisible = true

                endpicker = false

                endDatePicker.isVisible = false
                endTimePicker.isVisible = false

                memo.clearFocus()
                title.clearFocus()

                startDatePicker.init(
                    startCal.get(Calendar.YEAR),
                    startCal.get(Calendar.MONTH),
                    startCal.get(Calendar.DAY_OF_MONTH),
                    DatePicker.OnDateChangedListener { _, year, month, dayOfMonth ->
                        startCal.set(Calendar.YEAR, year)
                        startCal.set(Calendar.MONTH, month)
                        startCal.set(Calendar.DATE, dayOfMonth)
                        startDateText.text =
                            (startCal.get(Calendar.MONTH) + 1).toString() + "월 " + startCal.get(
                                Calendar.DATE
                            ).toString() + "일"
                    })

                startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    startCal.set(Calendar.HOUR, hourOfDay)
                    startCal.set(Calendar.MINUTE, minute)
                    AddScheduleInfo.settingStartCal()
                    startTimeText.text =
                        if (hourOfDay < 12 || hourOfDay == 24) {
                            "오전" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                                startCal.get(Calendar.MINUTE)
                            )
                        } else {
                            "오후" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                                startCal.get(Calendar.MINUTE)
                            )
                        }
                    endTimeText.text =
                        if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                            endCal.get(Calendar.MINUTE)
                        )
                        else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                            endCal.get(
                                Calendar.MINUTE
                            )
                        )
                }
            } else {
                startpicker = false

                startDatePicker.isVisible = false
                startTimePicker.isVisible = false
            }
        }

        //endpicker
        endView.setOnClickListener {
            if (!endpicker) {
                endpicker = true

                endDatePicker.isVisible = true
                endTimePicker.isVisible = true

                startpicker = false

                startDatePicker.isVisible = false
                startTimePicker.isVisible = false

                memo.clearFocus()
                title.clearFocus()

                endDatePicker.init(
                    endCal.get(Calendar.YEAR),
                    endCal.get(Calendar.MONTH),
                    endCal.get(Calendar.DAY_OF_MONTH),
                    DatePicker.OnDateChangedListener { _, year, month, dayOfMonth ->
                        endCal.set(Calendar.YEAR, year)
                        endCal.set(Calendar.MONTH, month)
                        endCal.set(Calendar.DATE, dayOfMonth)
                        endDateText.text =
                            (endCal.get(Calendar.MONTH) + 1).toString() + "월 " + endCal.get(Calendar.DATE)
                                .toString() + "일 "
                    })

                endTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    endCal.set(Calendar.HOUR, hourOfDay)
                    endCal.set(Calendar.MINUTE, minute)
                    AddScheduleInfo.settingEndCal()
                    endTimeText.text =
                        if (hourOfDay < 12 || hourOfDay == 24) {
                            "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                                endCal.get(Calendar.MINUTE)
                            )
                        } else {
                            "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                                endCal.get(Calendar.MINUTE)
                            )
                        }
                    startTimeText.text =
                        if (startCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                            startCal.get(Calendar.MINUTE)
                        )
                        else "오후" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                            startCal.get(
                                Calendar.MINUTE
                            )
                        )
                }
            } else {
                endpicker = false

                endDatePicker.isVisible = false
                endTimePicker.isVisible = false
            }
        }

        // 참석자초대로 넘어가는 버튼
        val inviters = view.findViewById<TextView>(R.id.inviters)
        if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text = ""
        } else if (AddScheduleInfo.invitersNumber == 1 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text = AddScheduleInfo.inviteMembers[0].nickname
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 1) {
            inviters.text = AddScheduleInfo.inviteGroups[0].nickname
        } else if (AddScheduleInfo.invitersNumber > 1 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text =
                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber - 1).toString() + "명"
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber > 1) {
            inviters.text =
                AddScheduleInfo.inviteGroups[0].nickname + " 외 " + (AddScheduleInfo.inviteGroupsNumber).toString() + "그룹"
        } else {
            inviters.text =
                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + ((AddScheduleInfo.invitersNumber - 1) + AddScheduleInfo.allGroupMembersNumber).toString() + "명"
        }
        val to_invite_button = view.findViewById<ConstraintLayout>(R.id.to_invite_button)
        to_invite_button.setOnClickListener(View.OnClickListener {
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
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
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
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
        colorName.text = colorTextSetting()
        colorSetting(colorIcon)
        to_colorPick_button.setOnClickListener(View.OnClickListener {
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
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

        // 일정 제목
        var titleClick = false
        title.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (!titleClick || AddScheduleInfo.title == "일정 제목") {
                        title.text = null
                    }
                    titleClick = true
                    endpicker = false
                    endDatePicker.isVisible = false
                    endTimePicker.isVisible = false
                    startpicker = false
                    startDatePicker.isVisible = false
                    startTimePicker.isVisible = false
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
        memo.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    endpicker = false
                    endDatePicker.isVisible = false
                    endTimePicker.isVisible = false
                    startpicker = false
                    startDatePicker.isVisible = false
                    startTimePicker.isVisible = false
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

        // 저장 버튼
        val saveButton = view.findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            AddScheduleInfo.title = title.text.toString()
            AddScheduleInfo.memo = memo.text.toString()

            val startCalTemp = startCal
            val endCalTemp = endCal
            val client = OkHttpClient()
            val bodyTemp = FormBody.Builder()
                .add("title", AddScheduleInfo.title)
                .add("memo", AddScheduleInfo.memo)
                .add("color", AddScheduleInfo.color.toString())
                .add("userid", AddScheduleInfo.userId)

            if (AddScheduleInfo.repeatType == "반복 안함") {
                val temp = bodyTemp
                val body = temp.add("startDate", saveCalSetting(startCalTemp))
                    .add("endDate", saveCalSetting(endCalTemp))
                    .build()
                val request: Request =
                    Request.Builder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/calendar").post(body).build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log1", e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun run() {
                                if (response.code() == 200) {
                                    saveState = true
                                    Log.v("test", "save success")
                                } else {
                                    Log.v("test2", "fail")
                                    saveState = false
                                    Toast
                                        .makeText(
                                            context,
                                            "오류가 발생햇습니다. 다시 시도해주세요",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        }.run()
                    }
                })
            } else if (AddScheduleInfo.repeatType == "매일") {
                for (i in 0 until 366) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매주") {
                for (i in 0 until 53) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 7)
                }
            } else if (AddScheduleInfo.repeatType == "매월") {
                for (i in 0 until 25) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.MONTH, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매년") {
                for (i in 0 until 21) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.YEAR, 1)
                }
            } else if (AddScheduleInfo.repeatType == "맞춤 설정") {
                if (AddScheduleInfo.repeatDetailType == "Days") {
                    for (i in 0 until AddScheduleInfo.repeatAllCount + 1) {
                        val temp = bodyTemp
                        val body = temp.add("startDate", saveCalSetting(startCalTemp))
                            .add("endDate", saveCalSetting(endCalTemp))
                            .build()
                        val request: Request =
                            Request.Builder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .url("http://3.35.146.57:3000/knock").post(body).build()
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.d("log1", e.message.toString())
                            }

                            override fun onResponse(call: Call, response: Response) {
                                object : Thread() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun run() {
                                        if (response.code() == 200) {
                                            saveState = true
                                        } else {
                                            saveState = false
                                            Toast
                                                .makeText(
                                                    context,
                                                    "오류가 발생햇습니다. 다시 시도해주세요",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }.run()
                            }
                        })
                        startCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
                    }
                } else if (AddScheduleInfo.repeatDetailType == "Weeks") {
//                    var daysTemp = mutableListOf<String>()
//                    for (i in 0 until AddScheduleInfo.repeatAllCount + 1) {
//                        val temp = bodyTemp
//                        val body = temp.add("startDate", saveCalSetting(startCalTemp))
//                            .add("endDate", saveCalSetting(endCalTemp))
//                            .build()
//                        val request: Request =
//                            Request.Builder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                .url("http://3.35.146.57:3000/calendar").post(body).build()
//                        client.newCall(request).enqueue(object : Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                Log.d("log1", e.message.toString())
//                            }
//
//                            override fun onResponse(call: Call, response: Response) {
//                                object : Thread() {
//                                    @SuppressLint("NotifyDataSetChanged")
//                                    override fun run() {
//                                        if (response.code() == 200) {
//                                            saveState = true
//                                        } else {
//                                            saveState = false
//                                            Toast
//                                                .makeText(
//                                                    context,
//                                                    "오류가 발생햇습니다. 다시 시도해주세요",
//                                                    Toast.LENGTH_LONG
//                                                )
//                                                .show()
//                                        }
//                                    }
//                                }.run()
//                            }
//                        })
//                        startCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
//                    }
                }
            }
            parentFragment?.childFragmentManager
                ?.beginTransaction()?.remove(this)?.commit()
            parentFragment?.requireActivity()?.supportFragmentManager
                ?.beginTransaction()?.remove(requireParentFragment())?.commit()
            (activity as MainActivity).resume()
        }

        val knockButton = view.findViewById<TextView>(R.id.knock_button)
        if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 0) {
            saveButton.visibility = View.VISIBLE
            knockButton.visibility = View.GONE
        } else {
            saveButton.visibility = View.GONE
            knockButton.visibility = View.VISIBLE
        }

        knockButton.setOnClickListener {
            AddScheduleInfo.title = title.text.toString()
            AddScheduleInfo.memo = memo.text.toString()
            val startCalTemp = startCal
            val endCalTemp = endCal
            val client = OkHttpClient()
            val bodyTemp = FormBody.Builder()
                .add("title", AddScheduleInfo.title)
                .add("memo", AddScheduleInfo.memo)
                .add("color", AddScheduleInfo.color.toString())
                .add("senderid", AddScheduleInfo.userId)

            for (i in 0 until AddScheduleInfo.inviteMembers.size) {
                bodyTemp.add("userid", AddScheduleInfo.inviteMembers[i].id!!)
            }

            for (i in 0 until AddScheduleInfo.inviteGroups.size) {
                bodyTemp.add("userid", AddScheduleInfo.inviteGroups[i].id!!)
            }

            if (requireArguments().getString("ScheduleType") == "KNOCK") {
                bodyTemp.add(
                    "startDate",
                    "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                        AddScheduleInfo.startCal.get(Calendar.MONTH)
                    }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                        AddScheduleInfo.startCal.get(
                            Calendar.HOUR
                        )
                    }:00:00"
                )
                    .add(
                        "endDate",
                        "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.endCal.get(Calendar.MONTH)
                        }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                            AddScheduleInfo.endCal.get(
                                Calendar.HOUR
                            )
                        }:00:00"
                    )

                val request: Request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "인터넷 연결 불안정")

                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response.code() == 200) {
                                    saveState = true
                                } else {
                                    saveState = false
                                    Log.d("log", response.message())
                                }
                            }
                        }.run()
                    }
                })
            } /*else if (AddScheduleInfo.repeatType == "반복 안함") {
                val temp = bodyTemp
                val body = temp.add("startDate", saveCalSetting(startCalTemp))
                    .add("endDate", saveCalSetting(endCalTemp))
                    .build()
                val request: Request =
                    Request.Builder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/calendar").post(body).build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log1", e.message.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun run() {
                                if (response.code() == 200) {
                                    saveState = true
                                } else {
                                    saveState = false
                                    Toast
                                        .makeText(
                                            context,
                                            "오류가 발생햇습니다. 다시 시도해주세요",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        }.run()
                    }
                })
            } else if (AddScheduleInfo.repeatType == "매일") {
                for (i in 0 until 366) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매주") {
                for (i in 0 until 53) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 7)
                }
            } else if (AddScheduleInfo.repeatType == "매월") {
                for (i in 0 until 25) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.MONTH, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매년") {
                for (i in 0 until 21) {
                    val temp = bodyTemp
                    val body = temp.add("startDate", saveCalSetting(startCalTemp))
                        .add("endDate", saveCalSetting(endCalTemp))
                        .build()
                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/calendar").post(body).build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log1", e.message.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun run() {
                                    if (response.code() == 200) {
                                        saveState = true
                                    } else {
                                        saveState = false
                                        Toast
                                            .makeText(
                                                context,
                                                "오류가 발생햇습니다. 다시 시도해주세요",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.YEAR, 1)
                }
            } else if (AddScheduleInfo.repeatType == "맞춤 설정") {
                if (AddScheduleInfo.repeatDetailType == "Days") {
                    for (i in 0 until AddScheduleInfo.repeatAllCount + 1) {
                        val temp = bodyTemp
                        val body = temp.add("startDate", saveCalSetting(startCalTemp))
                            .add("endDate", saveCalSetting(endCalTemp))
                            .build()
                        val request: Request =
                            Request.Builder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .url("http://3.35.146.57:3000/knock").post(body).build()
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.d("log1", e.message.toString())
                            }

                            override fun onResponse(call: Call, response: Response) {
                                object : Thread() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun run() {
                                        if (response.code() == 200) {
                                            saveState = true
                                        } else {
                                            saveState = false
                                            Toast
                                                .makeText(
                                                    context,
                                                    "오류가 발생햇습니다. 다시 시도해주세요",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }.run()
                            }
                        })
                        startCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
                    }
                } else if (AddScheduleInfo.repeatDetailType == "Weeks") {
//                    var daysTemp = mutableListOf<String>()
//                    for (i in 0 until AddScheduleInfo.repeatAllCount + 1) {
//                        val temp = bodyTemp
//                        val body = temp.add("startDate", saveCalSetting(startCalTemp))
//                            .add("endDate", saveCalSetting(endCalTemp))
//                            .build()
//                        val request: Request =
//                            Request.Builder()
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                .url("http://3.35.146.57:3000/calendar").post(body).build()
//                        client.newCall(request).enqueue(object : Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                Log.d("log1", e.message.toString())
//                            }
//
//                            override fun onResponse(call: Call, response: Response) {
//                                object : Thread() {
//                                    @SuppressLint("NotifyDataSetChanged")
//                                    override fun run() {
//                                        if (response.code() == 200) {
//                                            saveState = true
//                                        } else {
//                                            saveState = false
//                                            Toast
//                                                .makeText(
//                                                    context,
//                                                    "오류가 발생햇습니다. 다시 시도해주세요",
//                                                    Toast.LENGTH_LONG
//                                                )
//                                                .show()
//                                        }
//                                    }
//                                }.run()
//                            }
//                        })
//                        startCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
//                    }
                }
            }*/

            if (saveState) {
                parentFragment?.childFragmentManager
                    ?.beginTransaction()?.remove(this)?.commit()
                parentFragment?.requireActivity()?.supportFragmentManager
                    ?.beginTransaction()?.remove(requireParentFragment())?.commit()
            }
        }

        val backButton = view.findViewById<TextView>(R.id.back_button)
        backButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()?.remove(this)?.commit()
            parentFragment?.requireActivity()?.supportFragmentManager
                ?.beginTransaction()?.remove(requireParentFragment())?.commit()
        }

        /*view.findViewById<TextView>(R.id.save_button).setOnClickListener {

        }*/


        return view
    }

    private fun colorSetting(v: ImageView) {
        if (AddScheduleInfo.color == 0) {
            Glide.with(this).load(R.drawable.color1).into(v)
        } else if (AddScheduleInfo.color == 1) {
            Glide.with(this).load(R.drawable.color2).into(v)
        } else if (AddScheduleInfo.color == 2) {
            Glide.with(this).load(R.drawable.color3).into(v)
        } else if (AddScheduleInfo.color == 3) {
            Glide.with(this).load(R.drawable.color4).into(v)
        } else if (AddScheduleInfo.color == 4) {
            Glide.with(this).load(R.drawable.color5).into(v)
        } else if (AddScheduleInfo.color == 5) {
            Glide.with(this).load(R.drawable.color6).into(v)
        } else if (AddScheduleInfo.color == 6) {
            Glide.with(this).load(R.drawable.color7).into(v)
        } else if (AddScheduleInfo.color == 7) {
            Glide.with(this).load(R.drawable.color8).into(v)
        } else if (AddScheduleInfo.color == 8) {
            Glide.with(this).load(R.drawable.color9).into(v)
        } else if (AddScheduleInfo.color == 9) {
            Glide.with(this).load(R.drawable.color10).into(v)
        } else if (AddScheduleInfo.color == 10) {
            Glide.with(this).load(R.drawable.color11).into(v)
        } else if (AddScheduleInfo.color == 11) {
            Glide.with(this).load(R.drawable.color12).into(v)
        }
    }

    private fun calSetting(i: Int): String {
        return if (i < 10) {
            "0$i"
        } else {
            i.toString()
        }
    }

    private fun saveCalSetting(cal: Calendar): String {
        return if (cal.get(Calendar.AM_PM) == 0) cal.get(Calendar.YEAR)
            .toString() + "-" + (cal.get(Calendar.MONTH) + 1).toString() + "-" +
                cal.get(Calendar.DATE).toString() + " " +
                calSetting(cal.get(Calendar.HOUR)) + ":" +
                calSetting(cal.get(Calendar.MINUTE)) + ":" +
                calSetting(cal.get(Calendar.SECOND))
        else cal.get(Calendar.YEAR).toString() + "-" +
                (cal.get(Calendar.MONTH) + 1).toString() + "-" +
                cal.get(Calendar.DATE).toString() + " " +
                (cal.get(Calendar.HOUR) + 12).toString() + ":" +
                calSetting(cal.get(Calendar.MINUTE)) + ":" +
                calSetting(cal.get(Calendar.SECOND))
    }

    private fun colorTextSetting(): String {
        if (AddScheduleInfo.color == 0) {
            return "레드"
        } else if (AddScheduleInfo.color == 1) {
            return "파스텔 레드"
        } else if (AddScheduleInfo.color == 2) {
            return "옐로우"
        } else if (AddScheduleInfo.color == 3) {
            return "파스텔 옐로우"
        } else if (AddScheduleInfo.color == 4) {
            return "그린"
        } else if (AddScheduleInfo.color == 5) {
            return "파스텔 그린"
        } else if (AddScheduleInfo.color == 6) {
            return "블루"
        } else if (AddScheduleInfo.color == 7) {
            return "파스텔 블루"
        } else if (AddScheduleInfo.color == 8) {
            return "어두운 그레이"
        } else if (AddScheduleInfo.color == 9) {
            return "짙은 그레이"
        } else if (AddScheduleInfo.color == 10) {
            return "그레이"
        } else if (AddScheduleInfo.color == 11) {
            return "밝은 그레이"
        } else {
            return ""
        }
    }
}