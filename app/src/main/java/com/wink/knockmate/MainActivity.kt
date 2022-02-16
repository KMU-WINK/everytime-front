package com.wink.knockmate

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DayAdapter
    lateinit var datas: MutableList<DayAdapter.DateData>
    var lastPosition: Int = 0
    lateinit var rows: MutableList<TableRow>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.day_recycler)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        adapter = DayAdapter(this)
        adapter.mainActivity = this
        recyclerView.adapter = adapter

        val date = Calendar.getInstance()
        date.set(Calendar.DAY_OF_WEEK, 1)
        datas = mutableListOf<DayAdapter.DateData>()
        rows = mutableListOf()
        datas.apply {
            date.add(Calendar.WEEK_OF_YEAR, -1)
            add(DayAdapter.DateData(date.clone() as Calendar))
            date.add(Calendar.WEEK_OF_YEAR, 1)
            add(DayAdapter.DateData(date.clone() as Calendar))
            date.add(Calendar.WEEK_OF_YEAR, 1)
            add(DayAdapter.DateData(date.clone() as Calendar))

            adapter.datas = datas
            adapter.notifyDataSetChanged()
        }
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
            rows.add(v)
            calendarTable.addView(v)
        }

        findViewById<ImageButton>(R.id.main_hamburger).setOnClickListener {
        }

        findViewById<ImageButton>(R.id.main_datedropdown).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.main_add).visibility = View.VISIBLE
        }

        findViewById<TextView>(R.id.main_add_cancel).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.main_add).visibility = View.GONE
        }

        findViewById<TextView>(R.id.main_add_ok).setOnClickListener {
            val picker = findViewById<DatePicker>(R.id.main_date_picker)
            var pickdate = picker.getDate()
            pickdate.add(Calendar.DATE, -(pickdate.get(Calendar.DAY_OF_WEEK) - 1))

            datas.clear()
            datas.apply {
                pickdate.add(Calendar.WEEK_OF_YEAR, -1)
                add(DayAdapter.DateData(pickdate.clone() as Calendar))
                pickdate.add(Calendar.WEEK_OF_YEAR, 1)
                add(DayAdapter.DateData(pickdate.clone() as Calendar))
                pickdate.add(Calendar.WEEK_OF_YEAR, 1)
                add(DayAdapter.DateData(pickdate.clone() as Calendar))

                adapter.datas = datas
                adapter.notifyDataSetChanged()
            }

            recyclerView.scrollToPosition(1)
            resetDayRecycler(1)
            findViewById<ConstraintLayout>(R.id.main_add).visibility = View.GONE
        }
    }

    private fun DatePicker.getDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar
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
                "http://3.35.146.57:3000/weekly?email=dy@test.com&year=${selected!!.get(Calendar.YEAR)}&month=${
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
            }
            var arr = data.getJSONArray("data")
            for (i: Int in 0 until arr.length()) {
                val calendar = Calendar.getInstance()
                calendar.set(
                    Calendar.DAY_OF_MONTH,
                    arr.getJSONObject(i).getString("startDate").split(' ')[0].split('-')[2].toInt()
                )
                calendar.set(
                    Calendar.MONTH,
                    arr.getJSONObject(i).getString("startDate")
                        .split(' ')[0].split('-')[1].toInt() - 1
                )
                calendar.set(
                    Calendar.YEAR,
                    arr.getJSONObject(i).getString("startDate").split(' ')[0].split('-')[0].toInt()
                )

                val time = arr.getJSONObject(i).getString("startDate").split(' ')[1].split(':')

                val params = TableRow.LayoutParams(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        44F, resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        60F,
                        resources.displayMetrics
                    ).toInt()
                )
                params.column = calendar.get(Calendar.DAY_OF_WEEK) - 1

                val cell =
                    layoutInflater.inflate(R.layout.calendar_cell, rows[time[0].toInt()], false)
                rows[time[0].toInt()].addView(cell, params)
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