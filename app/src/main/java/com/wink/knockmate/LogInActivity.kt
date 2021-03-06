package com.wink.knockmate

import android.content.Intent
import android.content.SharedPreferences
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
import org.json.JSONObject
import java.io.IOException

class LogInActivity : AppCompatActivity() {

    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val loginEmail: EditText by lazy {
        findViewById(R.id.loginEmail)
    }

    private val loginPassword: EditText by lazy {
        findViewById(R.id.loginPassword)
    }

    private val loginButton: Button by lazy {
        findViewById(R.id.loginButton)
    }

    private val slashedEye: ImageButton by lazy {
        findViewById(R.id.slashedEye)
    }

    private val openedEye: ImageButton by lazy {
        findViewById(R.id.openedEye)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        backButton.setOnClickListener {
            finish()
        }

        slashedEye.setOnClickListener {
            slashedEye.isVisible = false
            openedEye.isVisible = true
            loginPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        openedEye.setOnClickListener {
            openedEye.isVisible = false
            slashedEye.isVisible = true
            loginPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        loginEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                changeButton()
            }
        }

        loginPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (loginPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    slashedEye.isVisible = true
                } else {
                    openedEye.isVisible = true
                }
                changeButton()
            }
        }

        checkID()
    }

    private fun checkID(){
        val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
        val email = pref.getString("email", "").toString()
        val password = pref.getString("password", "").toString()

        if(email != "" && password != ""){
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changeButton() {
        if (loginEmail.text.toString().isNotEmpty() && loginPassword.text.toString().isNotEmpty()) {
            loginButton.background =
                this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            loginButton.setOnClickListener {
                Log.d("log", "button clicked")
                val email = loginEmail.text.toString()
                val password = loginPassword.text.toString()

                val client = OkHttpClient()
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build()
                val request: Request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/signin").post(body).build()

                Log.d("log", "build success")

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "????????? ?????? ?????????")
                        runOnUiThread {
                            Toast.makeText(
                                this@LogInActivity,
                                "????????? ????????? ??????????????????. ?????? ??????????????????.",
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
                                    Log.d("success", "success")

                                    // ?????? ???????????? ?????? ???????????? ???????????? ??????????????? preference??? ??????
                                    val pref = getSharedPreferences("loginInfo", MODE_PRIVATE)
                                    val editor = pref.edit()
                                    editor.putString("email", email)
                                    editor.putString("password", password)
                                    editor.apply()

                                    val client = OkHttpClient().newBuilder()
                                        .build()
                                    val request: Request = Request.Builder()
                                        .url("http://3.35.146.57:3000/user?query=${email}")
                                        .method("GET", null)
                                        .build()
                                    client.newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            Log.d("log", "????????? ?????? ?????????")
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            object : Thread() {
                                                override fun run() {
                                                    when {
                                                        response.code() == 200 -> {
                                                            val data = JSONObject(
                                                                response.body()?.string()
                                                            )
                                                            var arr = data.getJSONArray("data")
                                                            var nickname = arr.getJSONObject(0)
                                                                .getString("nickname")
                                                            editor.putString(
                                                                "nickname",
                                                                nickname
                                                            )
                                                            editor.apply()
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

                                    // ?????? ???????????? ??????
                                    val intent =
                                        Intent(this@LogInActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else if (response.code() == 201) {
                                    Log.d("email", email)
                                    Log.d("password", password)
                                    Log.d("log", "????????? ????????? ?????? ????????????")
                                    runOnUiThread {
                                        findViewById<TextView>(R.id.wrong).isVisible = true
                                        loginButton.background =
                                            this@LogInActivity.resources.getDrawable(R.drawable.signupbutton_background_gray)
                                        loginButton.setOnClickListener {}
                                    }
                                } else {
                                    Log.d("log", "????????? ??????")
                                }
                            }
                        }.run()
                    }
                })
            }
        } else {
            loginButton.background =
                this.resources.getDrawable(R.drawable.signupbutton_background_gray)
            loginButton.setOnClickListener {}
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { // ?????? ???????????? ?????? ????????? ?????? ?????? ?????? ?????? ??? ???????????? ????????? ???????????? ????????????.
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