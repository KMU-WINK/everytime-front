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
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ModifyScheduleActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modify_schedule)
        val handler = Handler(Looper.myLooper()!!)

        val mod = intent.getStringExtra("type")

        if (mod == "modify"){
            AddScheduleInfo.reset()
            val calendarId = intent.getStringExtra("calendarId")
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
                                    finish()
                                    handler.postDelayed(Runnable() {
                                        @Override
                                        fun run() {
                                            Toast
                                                .makeText(
                                                    baseContext,
                                                    "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }, 0)
                                }
                            } else {
                                finish()
                                handler.postDelayed(Runnable() {
                                    @Override
                                    fun run() {
                                        Toast
                                            .makeText(
                                                baseContext,
                                                "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }, 0)
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
                                Log.v("test", res.toString())
                                try {
                                    val resTemp = res.getJSONArray("data")
                                    for (i in 0 until resTemp.length()) {
                                        if (calendarId != AddScheduleInfo.userId) {
                                            AddScheduleInfo.invitedMembers.add(
                                                UserModel(
                                                    resTemp.getJSONObject(i).getString("id"),
                                                    resTemp.getJSONObject(i).getString("nickname"),
                                                    true,
                                                    resTemp.getJSONObject(i).getString("email"),
                                                    i,
                                                    true,
                                                )
                                            )
                                            AddScheduleInfo.invitersNumber++
                                        }
                                        startDateArr =
                                            resTemp.getJSONObject(i).getString("startDate")
                                                .split("-", " ", ":")
                                        endDateArr =
                                            resTemp.getJSONObject(i).getString("endDate")
                                                .split("-", " ", ":")
                                    }
                                } catch (e: Exception) {
                                    finish()
                                    handler.postDelayed(Runnable() {
                                        @Override
                                        fun run() {
                                            Toast
                                                .makeText(
                                                    baseContext,
                                                    "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }, 0)
                                }
                            } else {
                                finish()
                                handler.postDelayed(Runnable() {
                                    @Override
                                    fun run() {
                                        Toast
                                            .makeText(
                                                baseContext,
                                                "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }, 0)
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
                                handler.postDelayed(Runnable() {
                                    @Override
                                    fun run() {
                                        Toast
                                            .makeText(
                                                baseContext,
                                                "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }, 0)
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
                                                "test",
                                                i,
                                                false,
                                                true,
                                                1,
                                                resTemp.getJSONObject(i).getInt("isFav")
                                            )
                                        )
                                    }
                                }
                            } else {
                                handler.postDelayed(Runnable() {
                                    @Override
                                    fun run() {
                                        Toast
                                            .makeText(
                                                baseContext,
                                                "유저 정보를 가져올 수 없습니다. 다시 시도해주세요.",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                }, 0)
                            }
                        }
                    }.run()
                }
            })

            if (startDateArr != null) {
                AddScheduleInfo.startCal.set(
                    startDateArr!![0].toInt(),
                    startDateArr!![1].toInt(),
                    startDateArr!![2].toInt(),
                    startDateArr!![3].toInt(),
                    startDateArr!![4].toInt(),
                    startDateArr!![5].toInt()
                )
            }
            if (endDateArr != null) {
                AddScheduleInfo.endCal.set(
                    endDateArr!![0].toInt(),
                    endDateArr!![1].toInt(),
                    endDateArr!![2].toInt(),
                    endDateArr!![3].toInt(),
                    endDateArr!![4].toInt(),
                    endDateArr!![5].toInt()
                )
            }
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
        ).toString() + "일 (" + AddScheduleInfo.startDay + ")"
        startTimeText.text =
            if (startCal.get(Calendar.AM_PM) == 0) "오전" + " " +
                    calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(startCal.get(Calendar.MINUTE))
            else "오후" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                startCal.get(
                    Calendar.MINUTE
                )
            )
        endDateText.text =
            (endCal.get(Calendar.MONTH) + 1).toString() + "월 " + endCal.get(Calendar.DATE)
                .toString() + "일 (" + AddScheduleInfo.endDay + ")"
        endTimeText.text =
            if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(Calendar.MINUTE)
            )
            else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(
                    Calendar.MINUTE
                )
            )

        // startpicker
        val startView = findViewById<LinearLayout>(R.id.addschedule_start)
        val startPicker = findViewById<ConstraintLayout>(R.id.start_picker)
        val startDatePicker = findViewById<NumberPicker>(R.id.start_date_picker)
        val startAmOrPmPicker = findViewById<NumberPicker>(R.id.start_am_or_pm)
        val startTimePicker = findViewById<NumberPicker>(R.id.start_time_picker)
        val month_list =
            arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        val day_list1 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일", "29일", "30일", "31일"
        )
        val day_list2 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일", "29일", "30일"
        )
        val day_list3 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일", "23일",
            "24일", "25일", "26일", "27일", "28일"
        )
        val day_list4 = arrayOf(
            "1일", "2일", "3일", "4일", "5일", "6일", "7일", "8일", "9일", "10일", "11일", "12일",
            "13일", "14일", "15일", "16일", "17일", "18일", "19일", "20일", "21일", "22일",
            "23일", "24일", "25일", "26일", "27일", "28일", "29일"
        )
        val temp = mutableListOf<String>()
        for (i in month_list) {
            if (i === "1월" || i === "3월" || i === "5월" || i === "7월" || i === "8월" || i === "10월" || i === "12월") {
                for (j in day_list1)
                    temp.add(i + j)
            } else if (i === "2월") {
                for (j in day_list3)
                    temp.add(i + j)
            } else
                for (j in day_list2)
                    temp.add(i + j)
        }
        val hourAm = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val hourPm = arrayOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        val minute = arrayOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55)
        val temp2 = mutableListOf<String>()
        for (i in hourAm) {
            for (j in minute)
                temp2.add(i.toString() + ":" + j.toString())
        }
        for (i in hourPm) {
            for (j in minute)
                temp2.add(i.toString() + ":" + j.toString())
        }
        // 윤년 케이스 추가해야함
        startDatePicker.displayedValues = temp.toTypedArray()
        startAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
        startTimePicker.displayedValues = temp2.toTypedArray()
        startDatePicker.value = (startCal.get(Calendar.MONTH) + 1).toString().toInt()
        if (startCal.get(Calendar.AM_PM) == Calendar.AM) {
            startAmOrPmPicker.value = 0
        } else {
            startAmOrPmPicker.value = 1
        }

        val endPicker = findViewById<ConstraintLayout>(R.id.end_picker)

        startView.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.VISIBLE
            endPicker.visibility = View.GONE
        })


        // endpicker
        val endView = findViewById<LinearLayout>(R.id.addschedule_end)
        val endDatePicker = findViewById<NumberPicker>(R.id.end_date_picker)
        val endAmOrPmPicker = findViewById<NumberPicker>(R.id.end_am_or_pm)
        val endTimePicker = findViewById<NumberPicker>(R.id.end_time_picker)
        // 윤년 케이스 추가해야함
        endDatePicker.displayedValues = temp.toTypedArray()
        endAmOrPmPicker.displayedValues = arrayOf("오전", "오후")
        endTimePicker.displayedValues = temp2.toTypedArray()
        endDatePicker.value = (endCal.get(Calendar.MONTH) + 1).toString().toInt()
        if (endCal.get(Calendar.AM_PM) == Calendar.AM) {
            endAmOrPmPicker.value = 0
        } else {
            endAmOrPmPicker.value = 1
        }
        endView.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.VISIBLE
        })

        // 일정 제목
        val title = findViewById<EditText>(R.id.schedule_title)
        var titleClick = false
        title.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if (!titleClick || AddScheduleInfo.title == "일정 제목") {
                        title.text = null
                    }
                    titleClick = true
                    startPicker.visibility = View.GONE
                    endPicker.visibility = View.GONE
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
        val memo = findViewById<EditText>(R.id.memo)
        memo.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    startPicker.visibility = View.GONE
                    endPicker.visibility = View.GONE
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
            inviters.text = AddScheduleInfo.inviteMembers[0].nickname
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber == 1) {
            inviters.text = AddScheduleInfo.inviteGroups[0].nickname
        } else if (AddScheduleInfo.invitersNumber > 1 && AddScheduleInfo.inviteGroupsNumber == 0) {
            inviters.text =
                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + (AddScheduleInfo.invitersNumber - 1).toString() + "명"
        } else if (AddScheduleInfo.invitersNumber == 0 && AddScheduleInfo.inviteGroupsNumber > 1) {
            inviters.text =
                AddScheduleInfo.inviteGroups[0].nickname + " 외 " + (AddScheduleInfo.allGroupMembersNumber - AddScheduleInfo.inviteGroups[0].isFav).toString() + "명"
        } else {
            inviters.text =
                AddScheduleInfo.inviteMembers[0].nickname + " 외 " + ((AddScheduleInfo.invitersNumber - 1) + AddScheduleInfo.allGroupMembersNumber).toString() + "명"
        }
        val to_invite_button = findViewById<ConstraintLayout>(R.id.to_invite_button)
        to_invite_button.setOnClickListener(View.OnClickListener {
            startPicker.visibility = View.GONE
            endPicker.visibility = View.GONE
            val intent = Intent(this, Modify_invite::class.java)
            startActivity(intent)
            finish()
        })

    }

    private fun colorSetting(v: ImageView) {
        if (AddScheduleInfo.color == 1) {
            Glide.with(this).load(R.drawable.color1).into(v)
        } else if (AddScheduleInfo.color == 2) {
            Glide.with(this).load(R.drawable.color2).into(v)
        } else if (AddScheduleInfo.color == 3) {
            Glide.with(this).load(R.drawable.color3).into(v)
        } else if (AddScheduleInfo.color == 4) {
            Glide.with(this).load(R.drawable.color4).into(v)
        } else if (AddScheduleInfo.color == 5) {
            Glide.with(this).load(R.drawable.color5).into(v)
        } else if (AddScheduleInfo.color == 6) {
            Glide.with(this).load(R.drawable.color6).into(v)
        } else if (AddScheduleInfo.color == 7) {
            Glide.with(this).load(R.drawable.color7).into(v)
        } else if (AddScheduleInfo.color == 8) {
            Glide.with(this).load(R.drawable.color8).into(v)
        } else if (AddScheduleInfo.color == 9) {
            Glide.with(this).load(R.drawable.color9).into(v)
        } else if (AddScheduleInfo.color == 10) {
            Glide.with(this).load(R.drawable.color10).into(v)
        } else if (AddScheduleInfo.color == 11) {
            Glide.with(this).load(R.drawable.color11).into(v)
        } else if (AddScheduleInfo.color == 12) {
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
}

