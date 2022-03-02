package com.wink.knockmate

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TestMain : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testpage)

        val button = findViewById<Button>(R.id.button)

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
    }
}