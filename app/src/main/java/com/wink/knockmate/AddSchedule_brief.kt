package com.wink.knockmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import okhttp3.*
import java.io.IOException
import java.util.*

class AddSchedule_brief : Fragment() {
    var saveState = false

    @SuppressLint("SetTextI18n")
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
        val startCal = AddScheduleInfo.startCal
        val endCal = AddScheduleInfo.endCal
        val icon = view.findViewById<ImageView>(R.id.icon)

        startDateText_brief.text =
            startCal.get(Calendar.YEAR).toString() + "년도 " +
                    (startCal.get(Calendar.MONTH) + 1).toString() + "월 " + startCal.get(
                Calendar.DATE
            ).toString() + "일(" + AddScheduleInfo.startDay + ")"
        startTimeText_brief.text =
            if (startCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(
                startCal.get(
                    Calendar.HOUR
                )
            ) + ":" + calSetting(startCal.get(Calendar.MINUTE))
            else "오후" + " " + calSetting(startCal.get(Calendar.HOUR)) + ":" + calSetting(
                startCal.get(
                    Calendar.MINUTE
                )
            )
        endTimeText_brief.text =
            if (endCal.get(Calendar.AM_PM) == 0) "오전" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(
                    Calendar.MINUTE
                )
            )
            else "오후" + " " + calSetting(endCal.get(Calendar.HOUR)) + ":" + calSetting(
                endCal.get(
                    Calendar.MINUTE
                )
            )

        brief_setting.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                ?.addToBackStack(null)
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.commit()
        }

        val title = view.findViewById<EditText>(R.id.schedule_title)
        var titleClick = false
        title.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (!titleClick || AddScheduleInfo.title == null) {
                    title.text = null
                }
                titleClick = true
            } else {
                if (AddScheduleInfo.title != null) {
                    title.setText("일정 제목")
                }
            }
        }

        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                AddScheduleInfo.title = title.text.toString()
            }
        })

        colorSetting(icon)

        val backButton = view.findViewById<TextView>(R.id.back_button)
        backButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()?.remove(this)?.commit()
            parentFragment?.requireActivity()?.supportFragmentManager
                ?.beginTransaction()?.remove(requireParentFragment())?.commit()
        }

        val saveButton = view.findViewById<TextView>(R.id.save_button)
        saveButton.setOnClickListener {
            val startCalTemp = startCal
            val endCalTemp = endCal
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("title", title.text.toString())
                .add("memo", AddScheduleInfo.memo)
                .add("color", AddScheduleInfo.color.toString())
                .add("userid", AddScheduleInfo.userId)
                .add("startDate", saveCalSetting(startCalTemp))
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
                                Log.v("test", "save success")
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

            parentFragment?.childFragmentManager
                ?.beginTransaction()?.remove(this)?.commit()
            parentFragment?.requireActivity()?.supportFragmentManager
                ?.beginTransaction()?.remove(requireParentFragment())?.commit()
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

            val body = bodyTemp.add("startDate", saveCalSetting(startCalTemp))
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

        }

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
}