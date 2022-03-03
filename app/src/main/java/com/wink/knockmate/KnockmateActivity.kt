package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class KnockmateActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DayAdapter
    lateinit var datas: MutableList<DayAdapter.DateData>
    lateinit var email: String
    lateinit var memo: String
    lateinit var groupid: String
    var calendarid: Int = 0
    var mode: Int = 0
    lateinit var nickname: String
    var lastPosition: Int = 0
    lateinit var rows: MutableList<TableRow>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knockmate)
        email = intent.extras?.getString("email").toString()
        calendarid = intent.extras!!.getInt("calendarid")
        mode = intent.extras!!.getInt("mode")
        nickname = intent.extras?.getString("nickname").toString()
        memo = intent.extras?.getString("memo").toString()
        groupid = intent.extras?.getString("groupid").toString()

        var cal = intent.extras?.getSerializable("start")
        cal = cal ?: Calendar.getInstance()
        (cal as Calendar).add(Calendar.DATE, -((cal as Calendar).get(Calendar.DAY_OF_WEEK) - 1))

        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        val myemail = pref.getString("email", "").toString()

        findViewById<TextView>(R.id.knockmate_datetext).text =
            "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월"
        findViewById<TextView>(R.id.knock_msg_content).text = memo

        if (groupid.isNotEmpty()) {
            findViewById<TextView>(R.id.knockmate_titletext).text = groupid
            findViewById<ConstraintLayout>(R.id.knock_noti_layout).visibility = View.GONE
            findViewById<ImageButton>(R.id.knockmate_menu).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.knockmate_group_footer_layout).visibility = View.VISIBLE
            Toast.makeText(this, "그룹이 생성되었습니다!", Toast.LENGTH_SHORT).show()
        } else if (mode == 1)
            findViewById<TextView>(R.id.knockmate_titletext).text = nickname + "님의 노크"
        else
            findViewById<TextView>(R.id.knockmate_titletext).text = nickname + "님의 일정"

        findViewById<ImageButton>(R.id.knock_accept).setOnClickListener {
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", myemail)
                .add("senderEmail", email)
                .add("calendarid", calendarid.toString())
                .build()
            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/accept_knock").post(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")

                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response.code() == 200) {
                                finish()
                            } else {
                                Log.d("log", response.message())
                            }
                        }
                    }.run()
                }
            })
        }

        findViewById<ImageButton>(R.id.knock_decline).setOnClickListener {
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", myemail)
                .add("senderEmail", email)
                .add("calendarid", calendarid.toString())
                .build()
            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/decline_knock").post(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")

                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response.code() == 200) {
                                finish()
                            } else {
                                Log.d("log", response.message())
                            }
                        }
                    }.run()
                }
            })
        }

        recyclerView = findViewById<RecyclerView>(R.id.day_recycler)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        adapter = DayAdapter(this)
        recyclerView.adapter = adapter

        val date = Calendar.getInstance()
        date.set(Calendar.DAY_OF_WEEK, 1)
        datas = mutableListOf<DayAdapter.DateData>()
        rows = mutableListOf()

        lastPosition = RecyclerView.NO_POSITION
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val view = snapHelper.findSnapView(recyclerView.layoutManager)!!
                    val position = recyclerView.layoutManager!!.getPosition(view)

                    if (lastPosition != position) {
                        lastPosition = position
                        resetDayRecycler(position)
                    }
                }
            }
        })

        val calendarTable = findViewById<TableLayout>(R.id.main_calendar)
        for (i: Int in 1..24) {
            val v = TableRow(applicationContext)
            v.clipChildren = false
            v.clipToPadding = false
            rows.add(v)
            calendarTable.addView(v)
        }

        findViewById<ImageButton>(R.id.knockmate_backbutton).setOnClickListener {
            finish()
        }

        datas.clear()
        datas.apply {
            cal.add(Calendar.WEEK_OF_YEAR, -1)
            add(DayAdapter.DateData(cal.clone() as Calendar))
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            add(DayAdapter.DateData(cal.clone() as Calendar))
            cal.add(Calendar.WEEK_OF_YEAR, 1)
            add(DayAdapter.DateData(cal.clone() as Calendar))

            adapter.datas = datas
            adapter.notifyDataSetChanged()
        }

        recyclerView.scrollToPosition(1)
        resetDayRecycler(1)
    }


    override fun onResume() {
        super.onResume()

        recyclerView.scrollToPosition(1)
    }

    fun resetDayRecycler(position: Int) {
        var selected: Calendar? = null
        datas.apply {
            if (position == 0) {
                val date = datas[0].date.clone() as Calendar
                date.add(Calendar.WEEK_OF_YEAR, -1)
                add(0, DayAdapter.DateData(date))

                selected = datas[1].date
            } else if (position == datas.size - 1) {
                val date = datas[datas.size - 1].date.clone() as Calendar
                date.add(Calendar.WEEK_OF_YEAR, 1)
                add(DayAdapter.DateData(date))

                selected = datas[datas.size - 1].date
            } else {
                selected = datas[position].date
            }
            adapter.datas = datas
            adapter.notifyDataSetChanged()
        }
        if (position == 0) {
            lastPosition = 1
            recyclerView.scrollToPosition(1)
        }

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(
                "http://3.35.146.57:3000/${
                    if (groupid.isEmpty()
                    ) "weekly?email=${email}" else "weeklygroup?groupid=${groupid}"
                }&year=${selected!!.get(Calendar.YEAR)}&month=${
                    selected!!.get(
                        Calendar.MONTH
                    ) + 1
                }&day=${selected!!.get(Calendar.DAY_OF_MONTH)}"
            )
            .method("GET", null)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "인터넷 연결 불안정")
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    override fun run() {
                        when {
                            response.code() == 200 -> {
                                updateCalendar(JSONObject(response.body()?.string()))
                            }
                            response.code() == 201 -> {
                            }
                            else -> {
                            }
                        }
                    }
                }.run()
            }
        })
    }

    fun updateCalendar(data: JSONObject) {
        runOnUiThread {
            for (i: Int in 1..24) {
                rows[i - 1].removeAllViews()

                val p = layoutInflater.inflate(
                    R.layout.calendar_cell_parent,
                    rows[i - 1],
                    false
                ) as FrameLayout

                p.updateLayoutParams<TableRow.LayoutParams> { width = 0 }
                p.updateLayoutParams<TableRow.LayoutParams> {
                    height = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        60F, resources.displayMetrics
                    ).toInt()
                }
                p.updateLayoutParams<TableRow.LayoutParams> { column = 0 }

                rows[i - 1].addView(p)
            }
            var arr = data.getJSONArray("data")
            for (i: Int in 0 until arr.length()) {
                val calendar =
                    Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
                val endCalendar =
                    Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)

                val time = arr.getJSONObject(i).getString("startDate").split(' ')[1].split(':')
                val endTime = arr.getJSONObject(i).getString("endDate").split(' ')[1].split(':')

                calendar.set(
                    Calendar.YEAR,
                    arr.getJSONObject(i).getString("startDate").split(' ')[0].split('-')[0].toInt()
                )
                calendar.set(
                    Calendar.MONTH,
                    arr.getJSONObject(i).getString("startDate")
                        .split(' ')[0].split('-')[1].toInt() - 1
                )
                calendar.set(
                    Calendar.DAY_OF_MONTH,
                    arr.getJSONObject(i).getString("startDate").split(' ')[0].split('-')[2].toInt()
                )
                calendar.set(Calendar.HOUR, time[0].toInt())
                calendar.set(Calendar.MINUTE, time[1].toInt())

                endCalendar.set(
                    Calendar.YEAR,
                    arr.getJSONObject(i).getString("endDate").split(' ')[0].split('-')[0].toInt()
                )
                endCalendar.set(
                    Calendar.MONTH,
                    arr.getJSONObject(i).getString("endDate")
                        .split(' ')[0].split('-')[1].toInt() - 1
                )
                endCalendar.set(
                    Calendar.DAY_OF_MONTH,
                    arr.getJSONObject(i).getString("endDate").split(' ')[0].split('-')[2].toInt()
                )
                endCalendar.set(Calendar.HOUR, endTime[0].toInt())
                endCalendar.set(Calendar.MINUTE, endTime[1].toInt())

                val t = (endCalendar.timeInMillis - calendar.timeInMillis) / (1000 * 60)

                var cellParent: FrameLayout? = null

                if (rows[time[0].toInt()].childCount == 1) {
                    cellParent = layoutInflater.inflate(
                        R.layout.calendar_cell_parent,
                        rows[time[0].toInt()],
                        false
                    ) as FrameLayout

                    cellParent.updateLayoutParams<TableRow.LayoutParams> {
                        column = calendar.get(Calendar.DAY_OF_WEEK)
                    }
                    rows[time[0].toInt()].addView(cellParent)
                } else {
                    cellParent = rows[time[0].toInt()].getChildAt(1) as FrameLayout
                }

                val cell =
                    layoutInflater.inflate(
                        R.layout.calendar_cell,
                        cellParent,
                        false
                    )

                cellParent.addView(cell)

                val pparams = cell.layoutParams as FrameLayout.LayoutParams
                pparams.height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    t.toFloat(),
                    resources.displayMetrics
                ).toInt()

                pparams.topMargin =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        time[1].toFloat(),
                        resources.displayMetrics
                    ).toInt()

                cell.layoutParams = pparams

                val img = cell.findViewById<ImageView>(R.id.calendar_cell_image)
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(baseContext, R.drawable.cell_rectangle)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(
                    wrappedDrawable,
                    resources.getColor(
                        arrayListOf<Int>(
                            R.color.err,
                            R.color.warn,
                            R.color.info,
                            R.color.safe
                        )[Random().nextInt(4)]
                    )
                )
                img.setImageDrawable(wrappedDrawable)
                val text = cell.findViewById<TextView>(R.id.calendar_cell_text)
                text.text = arr.getJSONObject(i).getString("title")
                arr.getJSONObject(i).getString("endDate")
            }
        }
    }
}