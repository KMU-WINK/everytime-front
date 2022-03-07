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
            args.putString("ScheduleType", "ADD") // 타입
            bottomSheetDialogFragment.arguments = args
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        })

        button2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@TestMain, ModifyScheduleActivity::class.java)
            AddScheduleInfo.reset()

            intent.putExtra("calendarId", "30") // 일정 아이디
            intent.putExtra("type", "modify") // 타입
            startActivity(intent)
        })

        button3.setOnClickListener(View.OnClickListener {
            val bottomSheetDialogFragment = AddSchedule()
            val args = Bundle()
            args.putString("ScheduleType", "KNOCK") // 타입
            args.putString("invite", "test") // 노크할 사람 혹은 그룹의 아이디
            args.putString("inviteType", "group") // 노크하는 게 그룹인지 사람인지 타입
            bottomSheetDialogFragment.arguments = args
            bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
        })
    }
}