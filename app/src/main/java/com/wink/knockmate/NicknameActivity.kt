package com.wink.knockmate

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import okhttp3.*
import java.io.IOException

class NicknameActivity : AppCompatActivity() {

    lateinit var email: String
    lateinit var password: String

    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val editNickname: EditText by lazy {
        findViewById(R.id.editNickname)
    }

    private val nextButton: Button by lazy {
        findViewById(R.id.nextButton)
    }

    private val message: TextView by lazy {
        findViewById(R.id.message)
    }

    private val short: TextView by lazy {
        findViewById(R.id.nicknameShort)
    }

    private val long: TextView by lazy {
        findViewById(R.id.nicknameLong)
    }

    private val wrong: TextView by lazy {
        findViewById(R.id.nicknameFormWrong)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()

        backButton.setOnClickListener {
            finish()
        }

        editNickname.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (editNickname.text.toString().isNotEmpty()) {
                    short.isVisible = false
                    long.isVisible = false
                    wrong.isVisible = false
                    message.isVisible = true
                    activateButton()
                } else {
                    inactivateButton()
                }
            }
        }
    }

    private fun checkNickname(): String { // 닉네임이 유효한지 검사
        val nickname = editNickname.text.toString()
        if (!isValid(nickname)) { // 닉네임 형식이 유효하지 않으면
            return "Wrong"
        } else if (nickname.length < 2) {
            return "Short"
        } else if (nickname.length > 12) {
            return "Long"
        } else {
            return "OK"
        }
    }

    private fun isValid(str: String): Boolean { // 닉네임이 한글, 영어, 숫자로만 이루어져있는지 확인
        var i = 0
        while (i < str.length) {
            val c = str.codePointAt(i)
            if (c in 0xAC00..0xD800 || c in 0x0041..0x005A || c in 0x0061..0x007A || c in 0x0030..0x0039) {
                i += Character.charCount(c)
            } else {
                return false
            }
        }
        return true
    }

    private fun activateButton() {
        nextButton.background =
            this.resources.getDrawable(R.drawable.signupbutton_background_orange)
        nextButton.setOnClickListener {
            if (checkNickname() == "OK") {
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .add("nickname", editNickname.text.toString())
                    .add("color", "1") // TODO 임시 테스트용
                    .build()

                val request: Request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/signup").post(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Log.d("log", e.toString())
                            Toast.makeText(
                                this@NicknameActivity,
                                "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object : Thread() {
                            override fun run() {
                                if (response.code() == 200) {
                                    Log.d("email", email)
                                    Log.d("password", password)
                                    Log.d("nickname", editNickname.text.toString())
                                    // TODO 다음 화면으로 이동
                                } else {
                                    runOnUiThread {
                                        Log.d("response code", response.code().toString())
                                        Log.d("email", email)
                                        Log.d("password", password)
                                        Log.d("nickname", editNickname.text.toString())
                                        Toast.makeText(
                                            this@NicknameActivity,
                                            "회원가입 실패",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }.run()
                    }
                })
            } else if (checkNickname() == "Wrong") {
                message.isVisible = false
                wrong.isVisible = true
                inactivateButton()
            } else if (checkNickname() == "Short") {
                message.isVisible = false
                short.isVisible = true
                inactivateButton()
            } else if (checkNickname() == "Long") {
                message.isVisible = false
                long.isVisible = true
                inactivateButton()
            }
        }
    }

    private fun inactivateButton() {
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