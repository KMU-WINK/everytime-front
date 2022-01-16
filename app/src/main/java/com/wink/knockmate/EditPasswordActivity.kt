package com.wink.knockmate

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
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

class EditPasswordActivity : AppCompatActivity(){

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }



    private val tempPassword : EditText by lazy{
        findViewById(R.id.tempPassword)
    }

    private val tempPasswordSlashedEye : ImageButton by lazy{
        findViewById(R.id.tempPasswordSlashedEye)
    }

    private val tempPasswordOpenedEye : ImageButton by lazy{
        findViewById(R.id.tempPasswordOpenedEye)
    }

    private val wrongTempPassword : TextView by lazy{
        findViewById(R.id.wrongTempPassword)
    }



    private val newPassword : EditText by lazy{
        findViewById(R.id.newPassword)
    }

    private val newPasswordSlashedEye : ImageButton by lazy{
        findViewById(R.id.newPasswordSlashedEye)
    }

    private val newPasswordOpenedEye : ImageButton by lazy{
        findViewById(R.id.newPasswordOpenedEye)
    }



    private val newPasswordCheck : EditText by lazy{
        findViewById(R.id.newPasswordCheck)
    }

    private val newPasswordCheckSlashedEye : ImageButton by lazy{
        findViewById(R.id.newPasswordCheckSlashedEye)
    }

    private val newPasswordCheckOpenedEye : ImageButton by lazy{
        findViewById(R.id.newPasswordCheckOpenedEye)
    }

    private val wrongNewPasswordCheck : TextView by lazy{
        findViewById(R.id.wrongNewPasswordCheck)
    }

    private val nextButton : Button by lazy{
        findViewById(R.id.completeButton)
    }



    private var tempPasswordFlag = false
    private var newPasswordFlag = false
    private var newPasswordCheckFlag = false

    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editpassword)

        backButton.setOnClickListener {
            finish()
        }

        tempPasswordSlashedEye.setOnClickListener {
            tempPasswordSlashedEye.isVisible = false
            tempPasswordOpenedEye.isVisible = true
            tempPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        tempPasswordOpenedEye.setOnClickListener {
            tempPasswordOpenedEye.isVisible = false
            tempPasswordSlashedEye.isVisible = true
            tempPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        newPasswordSlashedEye.setOnClickListener {
            newPasswordSlashedEye.isVisible = false
            newPasswordOpenedEye.isVisible = true
            newPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        newPasswordOpenedEye.setOnClickListener {
            newPasswordOpenedEye.isVisible = false
            newPasswordSlashedEye.isVisible = true
            newPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        newPasswordCheckSlashedEye.setOnClickListener {
            newPasswordCheckSlashedEye.isVisible = false
            newPasswordCheckOpenedEye.isVisible = true
            newPasswordCheck.inputType = InputType.TYPE_CLASS_TEXT
        }

        newPasswordCheckOpenedEye.setOnClickListener {
            newPasswordOpenedEye.isVisible = false
            newPasswordSlashedEye.isVisible = true
            newPasswordCheck.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        // email = pref.getString("email", "") // login branch와 merge하면 추가
        email = "iandr0805@gmail.com" // 임시 테스트용

        tempPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val checkIcon : ImageView = findViewById(R.id.tempPasswordCheckIcon)
            val xIcon : ImageView = findViewById(R.id.tempPasswordXIcon)

            if(!hasFocus){
                if(tempPassword.text.isNotEmpty()) {
                    if (tempPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        tempPasswordSlashedEye.isVisible = true
                    } else {
                        tempPasswordOpenedEye.isVisible = true
                    }
                }

                val password = tempPassword.text.toString()

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
                                this@EditPasswordActivity,
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

                                    tempPasswordFlag = true
                                    runOnUiThread {
                                        checkIcon.isVisible = true
                                        xIcon.isVisible = false
                                        wrongTempPassword.isVisible = false
                                        activateButton()
                                    }
                                } else if (response.code() == 201) {
                                    Log.d("email", email)
                                    Log.d("password", password)
                                    Log.d("log", "잘못된 비밀번호")

                                    tempPasswordFlag = false
                                    runOnUiThread {
                                        checkIcon.isVisible = false
                                        xIcon.isVisible = true
                                        wrongTempPassword.isVisible = true
                                        inactivateButton()
                                    }
                                } else {
                                    Log.d("log", "기존 비밀번호와 일치하지 않음")
                                }
                            }
                        }.run()
                    }
                })
            }
        }

        newPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val checkIcon : ImageView = findViewById(R.id.newPasswordCheckIcon)
            val xIcon : ImageView = findViewById(R.id.newPasswordXIcon)

            val checkIcon2 : ImageView = findViewById(R.id.newPasswordCheckCheckIcon)
            val xIcon2 : ImageView = findViewById(R.id.newPasswordCheckXIcon)

            if(!hasFocus){
                if(newPassword.text.isNotEmpty()) {
                    if (newPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        newPasswordSlashedEye.isVisible = true
                    } else {
                        newPasswordOpenedEye.isVisible = true
                    }
                }

                if(newPassword.text.length >= 4){
                    if(newPasswordCheck.text.isNotEmpty()){
                        if(newPassword.text.toString() == newPasswordCheck.text.toString()){
                            wrongNewPasswordCheck.isVisible = false

                            checkIcon2.isVisible = true
                            xIcon2.isVisible = false

                            newPasswordCheckFlag = true
                            activateButton()
                        } else{
                            wrongNewPasswordCheck.isVisible = true

                            checkIcon2.isVisible = false
                            xIcon2.isVisible = true

                            newPasswordCheckFlag = false
                            inactivateButton()
                        }
                    }

                    checkIcon.isVisible = true
                    xIcon.isVisible = false

                    newPasswordFlag = true
                    activateButton()
                } else{
                    checkIcon.isVisible = false
                    xIcon.isVisible = true

                    newPasswordFlag = false
                    inactivateButton()
                }
            }
        }

        newPasswordCheck.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val checkIcon : ImageView = findViewById(R.id.newPasswordCheckCheckIcon)
            val xIcon : ImageView = findViewById(R.id.newPasswordCheckXIcon)

            if(!hasFocus){
                if(newPasswordCheck.text.isNotEmpty()) {
                    if (newPasswordCheck.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        newPasswordCheckSlashedEye.isVisible = true
                    } else {
                        newPasswordCheckOpenedEye.isVisible = true
                    }
                }

                if(newPassword.text.toString() == newPasswordCheck.text.toString()){
                    wrongNewPasswordCheck.isVisible = false

                    checkIcon.isVisible = true
                    xIcon.isVisible = false

                    newPasswordCheckFlag = true
                    activateButton()
                } else{
                    wrongNewPasswordCheck.isVisible = true

                    checkIcon.isVisible = false
                    xIcon.isVisible = true

                    newPasswordCheckFlag = false
                    inactivateButton()
                }
            }
        }
    }

    private fun activateButton(){
        if(tempPasswordFlag && newPasswordFlag && newPasswordCheckFlag){
            nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            nextButton.setOnClickListener {
                val newPW = newPassword.text.toString()

                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("password", newPW).build()

                val request : Request = Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://3.35.146.57:3000/password").put(body).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        runOnUiThread {
                            Toast.makeText(
                                this@EditPasswordActivity,
                                "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        object: Thread(){
                            override fun run() {
                                if(response.code() == 200){
                                    runOnUiThread {
                                        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
                                        val editor = pref.edit()
                                        editor.putString("password", newPW)
                                        editor.apply()

                                        runOnUiThread {
                                            Toast.makeText(
                                                this@EditPasswordActivity,
                                                "비밀번호가 변경되었습니다.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                        }
                                    }
                                } else if(response.code() == 201){
                                    runOnUiThread {
                                        Log.d("code", response.code().toString())
                                        Log.d("password", newPW)
                                        Toast.makeText(this@EditPasswordActivity, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                }
                            }
                        }.run()
                    }
                })
            }
        }
    }

    private fun inactivateButton(){
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