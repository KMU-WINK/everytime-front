package com.wink.knockmate

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import android.telecom.Call
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import okhttp3.*
import java.io.IOException

class EditEmailActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }

    private val currentEmail : TextView by lazy{
        findViewById(R.id.tempEmail)
    }

    private val editEmail : EditText by lazy{
        findViewById(R.id.editEmail)
    }

    private val editPassword : EditText by lazy{
        findViewById(R.id.password)
    }

    private val wrongPassword : TextView by lazy{
        findViewById(R.id.wrongPassword)
    }

    private val nextButton : Button by lazy{
        findViewById(R.id.completeButton)
    }

    private val passwordSlashedEye : ImageButton by lazy{
        findViewById(R.id.slashedEye)
    }

    private val passwordOpenedEye : ImageButton by lazy{
        findViewById(R.id.openedEye)
    }

    private var emailFlag = false
    private var passwordFlag = false

    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editemail)

        backButton.setOnClickListener {
            finish()
        }

        // TODO 현재 이메일 주소를 currentEmail의 text에 넣어준다.
        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        // email = pref.getString("email", "") // login branch와 merge하면 추가
        email = "iandr0805@gmail.com" // 임시 테스트용
        currentEmail.text = email

        editEmail.onFocusChangeListener = View.OnFocusChangeListener{ _, hasFocus ->
            if(!hasFocus){ //이메일 입력 후 포커스를 잃은 뒤 이메일이 유효한지 체크
                val inputEmail : String = editEmail.text.toString()
                checkEmail(inputEmail)
            }
        }

        passwordSlashedEye.setOnClickListener {
            passwordSlashedEye.isVisible = false
            passwordOpenedEye.isVisible = true
            editPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        passwordOpenedEye.setOnClickListener {
            passwordOpenedEye.isVisible = false
            passwordSlashedEye.isVisible = true
            editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        editPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val checkIcon : ImageView = findViewById(R.id.passwordCheckIcon)
            val xIcon : ImageView = findViewById(R.id.passwordXIcon)

            if(!hasFocus){
                if(editPassword.text.isNotEmpty()){
                    if(editPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                        passwordSlashedEye.isVisible = true
                    } else{
                        passwordOpenedEye.isVisible = true
                    }

                    val password = editPassword.text.toString()

                    val client = OkHttpClient()
                    val body = FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build()
                    val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/signin").post(body).build()

                    client.newCall(request).enqueue(object: Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.d("log", "인터넷 연결 불안정")
                            runOnUiThread {
                                Toast.makeText(
                                    this@EditEmailActivity,
                                    "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onResponse(call: okhttp3.Call, response: Response) {
                            object : Thread() {
                                override fun run() {
                                    if (response.code() == 200) {
                                        Log.d("email", email)
                                        Log.d("password", password)
                                        Log.d("success", "success")

                                        passwordFlag = true
                                        runOnUiThread {
                                            checkIcon.isVisible = true
                                            xIcon.isVisible = false
                                            wrongPassword.isVisible = false
                                            activateButton()
                                        }
                                    } else if (response.code() == 201) {
                                        Log.d("email", email)
                                        Log.d("password", password)
                                        Log.d("log", "잘못된 이메일 또는 비밀번호")

                                        passwordFlag = false
                                        runOnUiThread {
                                            checkIcon.isVisible = false
                                            xIcon.isVisible = true
                                            wrongPassword.isVisible = true
                                            inactivateButton()
                                        }
                                    } else {
                                        Log.d("log", "로그인 실패")
                                    }
                                }
                            }.run()
                        }
                    })
                }
            }
        }
    }

    private fun checkEmail(email : String){
        val checkIcon : ImageView = findViewById(R.id.emailCheckIcon)
        val xIcon : ImageView = findViewById(R.id.emailXIcon)
        val wrongEmail : TextView = findViewById(R.id.wrongEmail)
        val unavailableEmail : TextView = findViewById(R.id.alreadyExist)

        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("email", email).build()

        val request : Request = Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://3.35.146.57:3000/check").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@EditEmailActivity,
                        "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                object: Thread(){
                    override fun run() {android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            if(response.code() == 200){
                                runOnUiThread {
                                    checkIcon.isVisible = true
                                    xIcon.isVisible = false
                                    wrongEmail.isVisible = false
                                    unavailableEmail.isVisible = false

                                    emailFlag = true
                                    activateButton()
                                }
                            } else if(response.code() == 201){
                                runOnUiThread {
                                    xIcon.isVisible = true
                                    unavailableEmail.isVisible = true // "이미 가입되어 있는 이메일입니다." 문구를 띄움
                                    checkIcon.isVisible = false
                                    wrongEmail.isVisible = false

                                    emailFlag = false
                                    inactivateButton()
                                }
                            }
                        } else{
                            runOnUiThread {
                                xIcon.isVisible = true
                                wrongEmail.isVisible = true // "올바르지 않은 이메일 형식입니다." 문구를 띄움
                                checkIcon.isVisible = false
                                unavailableEmail.isVisible = false

                                emailFlag = false
                                inactivateButton()
                            }
                        }
                    }
                }.run()
            }
        })
    }

    private fun activateButton(){ // 모든 정보를 기입 했는지 확인 후 버튼 활성화 여부를 결정한다.
        if(emailFlag && passwordFlag){
            nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            nextButton.setOnClickListener {
                val newEmail = editEmail.text.toString()
                // 서버로 변경된 이메일 전달
                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("targetEmail", newEmail)
                    .add("email", email)
                    .build()

                val request = Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://3.35.146.57:3000/email").put(body).build()

                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(
                                this@EditEmailActivity,
                                "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onResponse(call: okhttp3.Call, response: Response) {
                        if(response.code() == 200){
                            val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putString("email", newEmail)
                            editor.apply()
                            runOnUiThread {
                                Toast.makeText(this@EditEmailActivity, "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else{
                            runOnUiThread {
                                Log.d("code", response.code().toString())
                                Log.d("email", email)
                                Toast.makeText(this@EditEmailActivity, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun inactivateButton(){ // 버튼이 활성화 된 후 버튼을 다시 비활성화 해야할 때 사용되는 함수
        nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        nextButton.setOnClickListener{}
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