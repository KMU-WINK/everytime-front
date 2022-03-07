package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.addschedule_detail.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DayAdapter
    lateinit var datas: MutableList<DayAdapter.DateData>
    var lastPosition: Int = 0
    lateinit var rows: MutableList<TableRow>
    lateinit var email: String
    lateinit var nickname: String
    lateinit var snapHelper: SnapHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        email = pref.getString("email", "").toString()
        nickname = pref.getString("nickname", "").toString()
        findViewById<TextView>(R.id.main_caltext).text = nickname + "님의 일정"

        val navigationView = findViewById<NavigationView>(R.id.main_navigationView)
        val menu = navigationView.menu

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("http://3.35.146.57:3000/myfavfollower?email=${email}")
            .method("GET", null)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "인터넷 연결 불안정")
            }

            override fun onResponse(call: Call, response1: Response) {
                object : Thread() {
                    override fun run() {
                        if (response1.code() == 200) {
                            val client = OkHttpClient().newBuilder()
                                .build()
                            val request: Request = Request.Builder()
                                .url("http://3.35.146.57:3000/myfavgroup?email=${email}")
                                .method("GET", null)
                                .build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    Log.d("log", "인터넷 연결 불안정")
                                }

                                override fun onResponse(call: Call, response2: Response) {
                                    object : Thread() {
                                        override fun run() {
                                            when {
                                                response2.code() == 200 -> {
                                                    menu.clear()

                                                    val followerdata =
                                                        JSONObject(response1.body()?.string())
                                                    var followerarr =
                                                        followerdata.getJSONArray("data")
                                                    for (i: Int in 0 until followerarr.length()) {
                                                        val email =
                                                            followerarr.getJSONObject(i)
                                                                .getString("email")
                                                        val nickname =
                                                            followerarr.getJSONObject(i)
                                                                .getString("nickname")
                                                        menu.add(
                                                            followerarr.getJSONObject(i)
                                                                .getString("id")
                                                        )
                                                            .setOnMenuItemClickListener OnMenuItemClickListener@{


                                                                val intt = Intent(
                                                                    this@MainActivity,
                                                                    KnockmateActivity::class.java
                                                                )
                                                                intt.putExtra(
                                                                    "email",
                                                                    email
                                                                )
                                                                intt.putExtra(
                                                                    "nickname",
                                                                    nickname
                                                                )
                                                                startActivity(intt)
                                                                return@OnMenuItemClickListener false
                                                            }

                                                    }

                                                    val groupdata =
                                                        JSONObject(response2.body()?.string())
                                                    var grouparr =
                                                        groupdata.getJSONArray("data")
                                                    for (i: Int in 0 until grouparr.length()) {
                                                        val email =
                                                            grouparr.getJSONObject(i)
                                                                .getString("id")
                                                        menu.add(
                                                            grouparr.getJSONObject(i)
                                                                .getString("id")
                                                        )
                                                            .setOnMenuItemClickListener OnMenuItemClickListener@{


                                                                val intt = Intent(
                                                                    this@MainActivity,
                                                                    KnockmateActivity::class.java
                                                                )
                                                                intt.putExtra(
                                                                    "email",
                                                                    email
                                                                )
                                                                intt.putExtra(
                                                                    "nickname",
                                                                    email
                                                                )
                                                                startActivity(intt)
                                                                return@OnMenuItemClickListener false
                                                            }

                                                    }


                                                    navigationView.invalidate()
                                                }
                                            }
                                        }
                                    }.run()
                                }
                            })
                        }
                    }
                }.run()
            }
        })

        setSupportActionBar(findViewById(R.id.main_layout_toolbar)) // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_icon) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게
        findViewById<NavigationView>(R.id.main_navigationView).setNavigationItemSelectedListener(
            this@MainActivity
        )

        val header = navigationView.getHeaderView(0)
        header.findViewById<ImageButton>(R.id.drawer_setting)
            .setOnClickListener {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        header.findViewById<TextView>(R.id.userName).text = nickname
        header.findViewById<TextView>(R.id.userEmail).text = email

        recyclerView = findViewById<RecyclerView>(R.id.day_recycler)
        snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        adapter = DayAdapter(this)
        recyclerView.adapter = adapter

        val date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
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
        lastPosition = 1
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

        findViewById<ImageButton>(R.id.main_hamburger).setOnClickListener {
            findViewById<DrawerLayout>(R.id.main_drawer_layout).openDrawer(GravityCompat.START)
        }

        findViewById<ImageButton>(R.id.main_datedropdown).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.main_add).visibility = View.VISIBLE
        }

        findViewById<TextView>(R.id.main_add_cancel).setOnClickListener {
            findViewById<ConstraintLayout>(R.id.main_add).visibility = View.GONE
        }

        findViewById<ImageButton>(R.id.main_search).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.main_noti).setOnClickListener {
            val intent = Intent(this, NotationActivity::class.java)
            startActivity(intent)
        }


        findViewById<AppCompatButton>(R.id.main_floating).setOnClickListener {
            AddScheduleInfo.resetStartCal()
            val bottomSheetDialogFragment = AddSchedule()
            val args = Bundle()
            args.putString("ScheduleType", "ADD") // 일정 추가인지, 처음부터 노크인지, 일정 수정인지 판별
            bottomSheetDialogFragment.arguments = args
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                findViewById<DrawerLayout>(R.id.main_drawer_layout).openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() { //뒤로가기 처리
        if (findViewById<DrawerLayout>(R.id.main_drawer_layout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.main_drawer_layout).closeDrawers()
            // 테스트를 위해 뒤로가기 버튼시 Toast 메시지
            Toast.makeText(this, "back btn clicked", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return false
    }

    private fun DatePicker.getDate(): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
        calendar.set(year, month, dayOfMonth)
        return calendar
    }

    override fun onStart() {
        super.onStart()

        recyclerView.scrollToPosition(1)
        resetDayRecycler(1)
    }

    public fun resume() {
        resetDayRecycler(lastPosition)
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
        findViewById<TextView>(R.id.main_datetext).text = "${selected!!.get(Calendar.YEAR)}년 ${
            selected!!.get(
                Calendar.MONTH
            ) + 1
        }월"
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url(
                "http://3.35.146.57:3000/weekly?email=${email}&year=${selected!!.get(Calendar.YEAR)}&month=${
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
                    for (k: Int in 1..7) {
                        cellParent = layoutInflater.inflate(
                            R.layout.calendar_cell_parent,
                            rows[time[0].toInt()],
                            false
                        ) as FrameLayout

                        cellParent.updateLayoutParams<TableRow.LayoutParams> {
                            column = k
                        }
                        rows[time[0].toInt()].addView(cellParent)
                    }
                }
                cellParent =
                    rows[time[0].toInt()].getChildAt(calendar.get(Calendar.DAY_OF_WEEK)) as FrameLayout


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
                        )[arr.getJSONObject(i).getInt("color")]
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