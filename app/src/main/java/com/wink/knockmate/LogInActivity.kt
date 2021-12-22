package com.wink.knockmate

import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class LogInActivity : AppCompatActivity() {

    private val backButton : Button by lazy{
        findViewById(R.id.backButton)
    }

    private val loginEmail : EditText by lazy{
        findViewById(R.id.loginEmail)
    }

    private val loginPassword : EditText by lazy{
        findViewById(R.id.loginPassword)
    }

    private val loginButton : Button by lazy{
        findViewById(R.id.loginButton)
    }

    private val slashedEye : ImageButton by lazy {
        findViewById(R.id.slashedEye)
    }

    private val openedEye : ImageButton by lazy{
        findViewById(R.id.openedEye)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        slashedEye.setOnClickListener {
            slashedEye.isVisible = false
            openedEye.isVisible = true
            loginPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        openedEye.setOnClickListener {
            openedEye.isVisible = false
            slashedEye.isVisible = true
            loginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        loginEmail.onFocusChangeListener = View.OnFocusChangeListener{_, hasFocus ->
            if(!hasFocus){
                changeButton()
            }
        }

        loginPassword.onFocusChangeListener = View.OnFocusChangeListener{_, hasFocus ->
            if(!hasFocus){
                if(loginPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                    slashedEye.isVisible = true
                } else{
                    openedEye.isVisible = true
                }
                changeButton()
            }
        }
    }

    private fun changeButton(){
        if(loginEmail.text.toString().isNotEmpty() && loginPassword.text.toString().isNotEmpty()){
            loginButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            loginButton.setOnClickListener{
                if(checkLoginInfo()){
                    // TODO 다음 화면으로 이동
                } else{
                    findViewById<TextView>(R.id.wrong).isVisible = true
                    loginButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
                    loginButton.setOnClickListener {}
                }
            }
        } else{
            loginButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
            loginButton.setOnClickListener {}
        }
    }

    private fun checkLoginInfo() : Boolean{
        // TODO 입력된 정보를 서버에 보내서 가입된 이메일과 비밀번호가 맞는지 확인
        return true
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