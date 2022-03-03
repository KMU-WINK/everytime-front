package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TestMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testpage)

        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)

        // 테스트 프리퍼런스 실제로 할 때는 이거 없애면 됨
        val pref = getSharedPreferences("LoginInfo", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("email", "dy@test.com")
        editor.apply()

        button.setOnClickListener(View.OnClickListener {
            val bottomSheetDialogFragment = AddSchedule()
            val args = Bundle()
            args.putString("ScheduleType", "ADD") // 일정 추가인지, 처음부터 노크인지, 일정 수정인지 판별
            bottomSheetDialogFragment.arguments = args
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        })

        button2.setOnClickListener {
            val intent = Intent(this@TestMain, ModifyScheduleActivity::class.java)
            intent.putExtra("calendarId", "30")
            intent.putExtra("type", "modify")
            startActivity(intent)
        }

        button3.setOnClickListener(View.OnClickListener {
            val bottomSheetDialogFragment = AddSchedule()
            val args = Bundle()
            args.putString("ScheduleType", "KNOCK") // 일정 추가인지, 처음부터 노크인지, 일정 수정인지 판별
            args.putString("invite", "test")
            args.putString("inviteType", "group")
            bottomSheetDialogFragment.arguments = args
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        })
    }
}