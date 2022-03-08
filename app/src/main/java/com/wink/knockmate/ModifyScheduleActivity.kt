package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModifyScheduleActivity : AppCompatActivity() {
    private var calendarId = ""
    private var saveState = false
    private var startpicker = false
    private var endpicker = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modify_schedule)

        val mod = intent.getStringExtra("type")

        if (mod == "modify") {
            AddScheduleInfo.reset()
            calendarId = intent.getStringExtra("calendarId").toString()
            var startDateArr: List<String>? = null
            var endDateArr: List<String>? = null

            val prefUser = getSharedPreferences("LoginInfo", MODE_PRIVATE)
            val email = prefUser?.getString("email", "email")
            prefUser?.getString(
                "email",
                "email"
            )?.let { Log.v("test", it) }
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(
                    "http://3.35.146.57:3000/user?query=${email}"
                )
                .get()
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                Log.v("test", res.toString())
                                try {
                                    val resTemp = res.getJSONArray("data")
                                    AddScheduleInfo.userEmail =
                                        resTemp.getJSONObject(0).getString("email")
                                    AddScheduleInfo.userId =
                                        resTemp.getJSONObject(0).getString("id")
                                    AddScheduleInfo.color = resTemp.getJSONObject(0).getInt("color")
                                } catch (e: Exception) {
                                }
                            } else {
                            }
                        }
                    }.run()
                }
            })

            val request2: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(
                    "http://3.35.146.57:3000/calendaruser?id=${calendarId}"
                )
                .get()
                .build()
            client.newCall(request2).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                Log.v("test2", res.toString())
                                val resTemp = res.getJSONArray("data")
                                for (i in 0 until resTemp.length()) {
                                    if (AddScheduleInfo.userId != resTemp.getJSONObject(i)
                                            .getString("senderid")
                                    ) {
                                        if (resTemp.getJSONObject(i).getString("userid") != null) {
                                            val client2 = OkHttpClient().newBuilder()
                                                .build()
                                            val request5: Request = Request.Builder()
                                                .addHeader(
                                                    "Content-Type",
                                                    "application/x-www-form-urlencoded"
                                                )
                                                .url(
                                                    "http://3.35.146.57:3000/user?query=${
                                                        resTemp.getJSONObject(
                                                            i
                                                        ).getString("userid")
                                                    }"
                                                )
                                                .get()
                                                .build()
                                            client2.newCall(request5).enqueue(object : Callback {
                                                override fun onFailure(call: Call, e: IOException) {
                                                    Log.d("log1", e.message.toString())
                                                }

                                                override fun onResponse(
                                                    call: Call,
                                                    response: Response
                                                ) {
                                                    object : Thread() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        override fun run() {
                                                            if (response.code() == 200) {
                                                                val res2 = JSONObject(
                                                                    response.body()?.string()
                                                                )
                                                                Log.v("test", res2.toString())
                                                                val resTemp2 =
                                                                    res2.getJSONArray("data")
                                                                AddScheduleInfo.invitedMembers.add(
                                                                    UserModel(
                                                                        resTemp2.getJSONObject(i)
                                                                            .getString("id"),
                                                                        resTemp2.getJSONObject(i)
                                                                            .getString("nickname"),
                                                                        true,
                                                                        resTemp2.getJSONObject(i)
                                                                            .getString("email"),
                                                                        0,
                                                                        true,
                                                                        true,
                                                                        0,
                                                                        0,
                                                                        1,
                                                                        true,
                                                                        resTemp.getJSONObject(i)
                                                                            .getInt("accepted")
                                                                    )
                                                                )
                                                                AddScheduleInfo.invitersNumber++
                                                                AddScheduleInfo.priorInvitersNumber++
                                                                InviteData.userNum++
                                                            } else {
                                                            }
                                                        }
                                                    }.run()
                                                }
                                            })
                                        } else {
                                            AddScheduleInfo.invitedMembers.add(
                                                UserModel(
                                                    resTemp.getJSONObject(i).getString("groupid"),
                                                    null,
                                                    false,
                                                    "",
                                                    0,
                                                    true,
                                                    true,
                                                    0,
                                                    0,
                                                    1,
                                                    true,
                                                    resTemp.getJSONObject(i).getInt("accepted")
                                                )
                                            )
                                            AddScheduleInfo.inviteGroupsNumber++
                                            AddScheduleInfo.priorInviteGroupsNumber++
                                            InviteData.groupNum++
                                            val client2 = OkHttpClient().newBuilder()
                                                .build()
                                            val request5: Request = Request.Builder()
                                                .addHeader(
                                                    "Content-Type",
                                                    "application/x-www-form-urlencoded"
                                                )
                                                .url(
                                                    "http://3.35.146.57:3000/groupuserlist?groupid=${
                                                        resTemp.getJSONObject(
                                                            i
                                                        ).getString("groupid")
                                                    }"
                                                )
                                                .get()
                                                .build()
                                            client2.newCall(request5).enqueue(object : Callback {
                                                override fun onFailure(call: Call, e: IOException) {
                                                    Log.d("log6", e.message.toString())
                                                }

                                                override fun onResponse(
                                                    call: Call,
                                                    response: Response
                                                ) {
                                                    object : Thread() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        override fun run() {
                                                            if (response.code() == 200) {
                                                                val res =
                                                                    JSONObject(
                                                                        response.body()?.string()
                                                                    )
                                                                val resTemp =
                                                                    res.getJSONArray("data")
                                                                AddScheduleInfo.invitedMembers[i].members =
                                                                    resTemp.length()
                                                                AddScheduleInfo.allGroupMembersNumber += resTemp.length()
                                                                AddScheduleInfo.priorAllGroupMembersNumber += resTemp.length()
                                                                InviteData.allGroupNum += resTemp.length()
                                                            } else if (response.code() == 201) {
                                                            } else {
                                                            }
                                                        }
                                                    }.run()
                                                }
                                            })
                                        }
                                    }
                                }
                                startDateArr =
                                    resTemp.getJSONObject(0).getString("startDate")
                                        .split("-", " ", ":")
                                endDateArr =
                                    resTemp.getJSONObject(0).getString("endDate")
                                        .split("-", " ", ":")

                                Log.v("test3", startDateArr.toString())

                                AddScheduleInfo.startCal.set(
                                    startDateArr!![0].toInt(),
                                    startDateArr!![1].toInt() - 1,
                                    startDateArr!![2].toInt(),
                                    startDateArr!![3].toInt(),
                                    startDateArr!![4].toInt(),
                                    startDateArr!![5].toInt()
                                )

                                AddScheduleInfo.endCal.set(
                                    endDateArr!![0].toInt(),
                                    endDateArr!![1].toInt() - 1,
                                    endDateArr!![2].toInt(),
                                    endDateArr!![3].toInt(),
                                    endDateArr!![4].toInt(),
                                    endDateArr!![5].toInt()
                                )
                            } else {
                            }
                        }
                    }.run()
                }
            })

            val request3: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/myfollower?email=${email}")
                .get()
                .build()
            client.newCall(request3).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log2", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                val resTemp = res.getJSONArray("data")
                                AddScheduleInfo.followerList.apply {
                                    for (i in 0 until resTemp.length()) {
                                        AddScheduleInfo.followerList.add(
                                            UserModel(
                                                resTemp.getJSONObject(i).getString("id"),
                                                resTemp.getJSONObject(i).getString("nickname"),
                                                true,
                                                resTemp.getJSONObject(i).getString("email"),
                                                i
                                            )
                                        )
                                    }
                                }
                            } else {
                            }
                        }
                    }.run()
                }
            })

            val request4: Request = Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/mygroup?email=${email}")
                .get()
                .build()
            client.newCall(request4).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log3", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                                val res = JSONObject(response.body()?.string())
                                val resTemp = res.getJSONArray("data")
                                AddScheduleInfo.groupList.apply {
                                    for (i in 0 until resTemp.length()) {
                                        AddScheduleInfo.groupList.add(
                                            UserModel(
                                                resTemp.getJSONObject(i).getString("id"),
                                                resTemp.getJSONObject(i).getString("nickname"),
                                                false,
                                                "",
                                                i,
                                            )
                                        )
                                    }
                                }
                                for (i in 0 until AddScheduleInfo.groupList.size) {
                                    val temp = AddScheduleInfo.groupList[i].id
                                    val client2 = OkHttpClient().newBuilder()
                                        .build()
                                    val request5: Request = Request.Builder()
                                        .addHeader(
                                            "Content-Type",
                                            "application/x-www-form-urlencoded"
                                        )
                                        .url("http://3.35.146.57:3000/groupuserlist?groupid=${temp}")
                                        .get()
                                        .build()
                                    client2.newCall(request5).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            Log.d("log6", e.message.toString())
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            object : Thread() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                override fun run() {
                                                    if (response.code() == 200) {
                                                        val res =
                                                            JSONObject(response.body()?.string())
                                                        val resTemp = res.getJSONArray("data")
                                                        AddScheduleInfo.groupList[i].members =
                                                            resTemp.length()
                                                    } else if (response.code() == 201) {
                                                    } else {
                                                    }
                                                }
                                            }.run()
                                        }
                                    })
                                }
                            } else {
                            }
                        }
                    }.run()
                }
            })
        }

        val addscheduleIcon = findViewById<ImageView>(R.id.addschedule_icon)
        colorSetting(addscheduleIcon)

        val startDateText = findViewById<TextView>(R.id.detail_start_date)
        val startTimeText = findViewById<TextView>(R.id.detail_start_time)
        val endDateText = findViewById<TextView>(R.id.detail_end_date)
        val endTimeText = findViewById<TextView>(R.id.detail_end_time)
        val startCal = AddScheduleInfo.startCal
        val endCal = AddScheduleInfo.endCal

        startDateText.text = (startCal.get(Calendar.MONTH) + 1).toString() + "월 " + startCal.get(
            Calendar.DATE
        ).toString() + "일 (" + dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)) + ")"
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
                .toString() + "일 (" + dayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)) + ")"
        endTimeText.text =
            if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(Calendar.MINUTE)
            )
            else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(
                    Calendar.MINUTE
                )
            )

        val memo = findViewById<EditText>(R.id.memo)
        val title = findViewById<EditText>(R.id.schedule_title)

        //startpicker
        val startView = findViewById<LinearLayout>(R.id.addschedule_start)
        val endView = findViewById<LinearLayout>(R.id.addschedule_end)
        val startDatePicker = findViewById<DatePicker>(R.id.start_picker_date)
        val startTimePicker = findViewById<TimePicker>(R.id.start_picker_time)
        val endDatePicker = findViewById<DatePicker>(R.id.end_picker_date)
        val endTimePicker = findViewById<TimePicker>(R.id.end_picker_time)
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
                        AddScheduleInfo.startCal = startCal
                        AddScheduleInfo.settingStartCal()
                        startDateText.text =
                            (startCal.get(Calendar.MONTH) + 1).toString() + "월 " + startCal.get(
                                Calendar.DATE
                            )
                                .toString() + "일 (" + dayOfWeek(startCal.get(Calendar.DAY_OF_WEEK)) + ")"
                    })

                startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    startCal.set(Calendar.HOUR, hourOfDay)
                    startCal.set(Calendar.MINUTE, minute)
                    AddScheduleInfo.startCal = startCal
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
                        if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(
                            endCal.get(
                                Calendar.HOUR
                            )
                        ) + ":" + calSetting(
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
                        AddScheduleInfo.endCal = endCal
                        AddScheduleInfo.settingEndCal()
                        endDateText.text =
                            (endCal.get(Calendar.MONTH) + 1).toString() + "월 " + endCal.get(Calendar.DATE)
                                .toString() + "일 (" + dayOfWeek(endCal.get(Calendar.DAY_OF_WEEK)) + ")"
                    })

                endTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                    endCal.set(Calendar.HOUR, hourOfDay)
                    endCal.set(Calendar.MINUTE, minute)
                    AddScheduleInfo.endCal = endCal
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
                    endTimeText.text =
                        if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(
                            endCal.get(
                                Calendar.HOUR
                            )
                        ) + ":" + calSetting(
                            endCal.get(Calendar.MINUTE)
                        )
                        else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                            endCal.get(
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


        // 참석자초대로 넘어가는 버튼
        val inviters = findViewById<TextView>(R.id.inviters)
        if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text = ""
        } else if (AddScheduleInfo.invitersNumber == 1 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text = AddScheduleInfo.invitedMembers[0].nickname
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 1) {
            inviters.text = AddScheduleInfo.invitedMembers[0].nickname
        } else if (AddScheduleInfo.invitersNumber > 1 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text =
                AddScheduleInfo.invitedMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber - 1).toString() + "명"
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber > 1) {
            inviters.text =
                AddScheduleInfo.invitedMembers[0].nickname + " 외 " + (AddScheduleInfo.allGroupMembersNumber - AddScheduleInfo.inviteGroups[0].isFav).toString() + "명"
        } else {
            inviters.text =
                AddScheduleInfo.invitedMembers[0].nickname + " 외 " + ((AddScheduleInfo.invitersNumber - 1) + AddScheduleInfo.allGroupMembersNumber).toString() + "명"
        }
        val to_invite_button = findViewById<ConstraintLayout>(R.id.to_invite_button)
        to_invite_button.setOnClickListener(View.OnClickListener {
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        })

        // 반복 일정으로 넘어가는 버튼
        val repeatText = findViewById<TextView>(R.id.repeat_text)
        repeatText.text = AddScheduleInfo.repeatType
        val to_repeat_button = findViewById<ConstraintLayout>(R.id.to_repeat_button)
        to_repeat_button.setOnClickListener(View.OnClickListener {
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
            val intent = Intent(this, Modify_repeat::class.java)
            startActivity(intent)
            finish()
        })


        // 일정 색상 선택으로 넘어가는 버튼
        colorSetting(addscheduleIcon)
        val to_colorPick_button = findViewById<ConstraintLayout>(R.id.to_colorPick_button)
        val colorName = findViewById<TextView>(R.id.pick_color_name)
        val colorIcon = findViewById<ImageView>(R.id.color_icon)
        colorName.text = colorTextSetting()
        colorSetting(colorIcon)
        to_colorPick_button.setOnClickListener(View.OnClickListener {
            endpicker = false
            endDatePicker.isVisible = false
            endTimePicker.isVisible = false
            startpicker = false
            startDatePicker.isVisible = false
            startTimePicker.isVisible = false
            val intent = Intent(this, Modify_colorpick::class.java)
            startActivity(intent)
            finish()
        })


        val backButton = findViewById<TextView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val saveButton = findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            AddScheduleInfo.title = title.text.toString()
            AddScheduleInfo.memo = memo.text.toString()

            val client = OkHttpClient()
            val bodyTemp = FormBody.Builder()
                .add("title", AddScheduleInfo.title)
                .add("memo", AddScheduleInfo.memo)
                .add("color", AddScheduleInfo.color.toString())
                .add("userid", AddScheduleInfo.userId)
                .add("id", calendarId)

            for (i in 0 until AddScheduleInfo.invitedMembers.size) {
                if (AddScheduleInfo.invitedMembers[i].user) {
                    bodyTemp.add("userid", AddScheduleInfo.inviteMembers[i].id!!)
                } else {
                    bodyTemp.add("groupid", AddScheduleInfo.inviteMembers[i].id!!)
                }
            }

            bodyTemp.add(
                "startDate",
                "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                    AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                    AddScheduleInfo.startCal.get(
                        Calendar.HOUR
                    )
                }:00:00"
            )
                .add(
                    "endDate",
                    "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                        AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                    }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                        AddScheduleInfo.endCal.get(
                            Calendar.HOUR
                        )
                    }:00:00"
                )

            val body = bodyTemp.build()
            val request: Request =
                Request.Builder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/calendar").put(body).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log1", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun run() {
                            if (response.code() == 200) {
                            } else {
                            }
                        }
                    }.run()
                }
            })
            finish()
        }

        val knockButton = findViewById<TextView>(R.id.knock_button)
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
                bodyTemp.add("groupid", AddScheduleInfo.inviteGroups[i].id!!)
            }

            if (AddScheduleInfo.repeatType == "반복 안함") {
                bodyTemp.add(
                    "startDate",
                    "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                        AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                    }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                        AddScheduleInfo.startCal.get(
                            Calendar.HOUR
                        )
                    }:00:00"
                )
                    .add(
                        "endDate",
                        "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
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
//                                    val act = activity
//                                    activity?.runOnUiThread {
//                                        (act as KnockmateActivity).knockable = false
//                                        (act as KnockmateActivity).knocks.forEachIndexed { index, pair ->
//                                            val v =
//                                                ((act as KnockmateActivity).rows[pair.second].getChildAt(
//                                                    pair.first
//                                                ) as FrameLayout)
//                                            v.getChildAt(v.childCount - 1)
//                                                .setBackgroundResource(R.drawable.cell_rectangle)
//                                        }
//                                        act?.findViewById<TextView>(R.id.knockmate_knock_info_text)?.visibility =
//                                            View.VISIBLE
//                                        act?.findViewById<TextView>(R.id.knockmate_knock_info_text)?.text =
//                                            "노크를 신청하였습니다."
//                                        act?.findViewById<AppCompatButton>(R.id.knockmate_knock_info_button)?.visibility =
//                                            View.GONE
//                                    }
                                } else {
                                    saveState = false
                                    Log.d("log", response.message())
                                }
                            }
                        }.run()
                    }
                })
            } else if (AddScheduleInfo.repeatType == "매일") {
                for (i in 0 until 366) {
                    bodyTemp.add(
                        "startDate",
                        "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                        }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                            AddScheduleInfo.startCal.get(
                                Calendar.HOUR
                            )
                        }:00:00"
                    )
                        .add(
                            "endDate",
                            "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                                AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                            }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                                AddScheduleInfo.endCal.get(
                                    Calendar.HOUR
                                )
                            }:00:00"
                        )

                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")

                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                    } else {
                                        saveState = false
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 1)
                    endCal.add(Calendar.DATE, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매주") {
                for (i in 0 until 53) {
                    bodyTemp.add(
                        "startDate",
                        "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                        }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                            AddScheduleInfo.startCal.get(
                                Calendar.HOUR
                            )
                        }:00:00"
                    )
                        .add(
                            "endDate",
                            "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                                AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                            }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                                AddScheduleInfo.endCal.get(
                                    Calendar.HOUR
                                )
                            }:00:00"
                        )

                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")

                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                    } else {
                                        saveState = false
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.DATE, 7)
                    endCal.add(Calendar.DATE, 7)
                }
            } else if (AddScheduleInfo.repeatType == "매월") {
                for (i in 0 until 25) {
                    bodyTemp.add(
                        "startDate",
                        "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                        }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                            AddScheduleInfo.startCal.get(
                                Calendar.HOUR
                            )
                        }:00:00"
                    )
                        .add(
                            "endDate",
                            "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                                AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                            }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                                AddScheduleInfo.endCal.get(
                                    Calendar.HOUR
                                )
                            }:00:00"
                        )

                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")

                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                    } else {
                                        saveState = false
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.MONTH, 1)
                    endCal.add(Calendar.MONTH, 1)
                }
            } else if (AddScheduleInfo.repeatType == "매년") {
                for (i in 0 until 21) {
                    bodyTemp.add(
                        "startDate",
                        "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                            AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                        }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                            AddScheduleInfo.startCal.get(
                                Calendar.HOUR
                            )
                        }:00:00"
                    )
                        .add(
                            "endDate",
                            "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                                AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                            }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                                AddScheduleInfo.endCal.get(
                                    Calendar.HOUR
                                )
                            }:00:00"
                        )

                    val request: Request =
                        Request.Builder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")

                        }

                        override fun onResponse(call: Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                    } else {
                                        saveState = false
                                        Log.d("log", response.message())
                                    }
                                }
                            }.run()
                        }
                    })
                    startCal.add(Calendar.YEAR, 1)
                    endCal.add(Calendar.YEAR, 1)
                }
            } else if (AddScheduleInfo.repeatType == "맞춤 설정") {
                if (AddScheduleInfo.repeatDetailType == "Days") {
                    for (i in 0 until AddScheduleInfo.repeatAllCount + 1) {
                        bodyTemp.add(
                            "startDate",
                            "${AddScheduleInfo.startCal.get(Calendar.YEAR)}-${
                                AddScheduleInfo.startCal.get(Calendar.MONTH) + 1
                            }-${AddScheduleInfo.startCal.get(Calendar.DAY_OF_MONTH)} ${
                                AddScheduleInfo.startCal.get(
                                    Calendar.HOUR
                                )
                            }:00:00"
                        )
                            .add(
                                "endDate",
                                "${AddScheduleInfo.endCal.get(Calendar.YEAR)}-${
                                    AddScheduleInfo.endCal.get(Calendar.MONTH) + 1
                                }-${AddScheduleInfo.endCal.get(Calendar.DAY_OF_MONTH)} ${
                                    AddScheduleInfo.endCal.get(
                                        Calendar.HOUR
                                    )
                                }:00:00"
                            )

                        val request: Request =
                            Request.Builder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .url("http://3.35.146.57:3000/knock").post(bodyTemp.build()).build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.d("log", "인터넷 연결 불안정")

                            }

                            override fun onResponse(call: Call, response: Response) {
                                object : Thread() {
                                    override fun run() {
                                        if (response.code() == 200) {
                                        } else {
                                            saveState = false
                                            Log.d("log", response.message())
                                        }
                                    }
                                }.run()
                            }
                        })
                        startCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
                        endCal.add(Calendar.DATE, AddScheduleInfo.repeatInterval)
                    }
                } else if (AddScheduleInfo.repeatDetailType == "Weeks") {
                }
            }
            finish()
        }
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

    private fun dayOfWeek(d: Int): String {
        return when (d) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> " "
        }
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

