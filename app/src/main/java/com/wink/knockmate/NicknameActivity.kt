package com.wink.knockmate

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NicknameActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }

    private val editNickname : EditText by lazy{
        findViewById(R.id.editNickname)
    }

    private val nextButton : Button by lazy{
        findViewById(R.id.nextButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        backButton.setOnClickListener {
            finish()
        }

        editNickname.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                if (editNickname.text.toString().isNotEmpty()){
                    activateButton()
                } else{
                    inactivateButton()
                }
            }
        }
    }

    fun activateButton(){
        nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
        nextButton.setOnClickListener {
            // TODO 닉네임 서버로 전송
            // TODO 다음 화면으로 이동
        }
    }

    fun inactivateButton(){
        nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        nextButton.setOnClickListener {}
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { // 현재 포커스된 뷰의 영역이 아닌 다른 곳을 클릭 시 키보드를 내리고 포커스를 해제한다.
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}