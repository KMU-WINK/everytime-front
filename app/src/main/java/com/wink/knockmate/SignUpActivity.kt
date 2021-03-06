package com.wink.knockmate

import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import okhttp3.*
import org.w3c.dom.Text
import java.io.IOException


class SignUpActivity : AppCompatActivity() {

    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val editEmail: EditText by lazy {
        findViewById(R.id.signUpEmail)
    }

    private val editPassword: EditText by lazy {
        findViewById(R.id.signUpPassword)
    }

    private val editPasswordCheck: EditText by lazy {
        findViewById(R.id.signUpPasswordCheck)
    }

    private val nextButton: Button by lazy {
        findViewById(R.id.signUpButton)
    }

    var emailFlag = false
    var passwordFlag = false
    var passwordCheckFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        backButton.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        val passwordSlashedEye: ImageButton = findViewById(R.id.passwordSlashedEye)
        val passwordOpenedEye: ImageButton = findViewById(R.id.passwordOpenedEye)

        val passwordCheckSlashedEye: ImageButton = findViewById(R.id.passwordCheckSlashedIcon)
        val passwordCheckOpenedEye: ImageButton = findViewById(R.id.passwordCheckOpenedEye)

        passwordSlashedEye.setOnClickListener {
            passwordSlashedEye.isVisible = false
            passwordOpenedEye.isVisible = true
            editPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        passwordOpenedEye.setOnClickListener {
            passwordOpenedEye.isVisible = false
            passwordSlashedEye.isVisible = true
            editPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        passwordCheckSlashedEye.setOnClickListener {
            passwordCheckSlashedEye.isVisible = false
            passwordCheckOpenedEye.isVisible = true
            editPasswordCheck.inputType = InputType.TYPE_CLASS_TEXT
        }

        passwordCheckOpenedEye.setOnClickListener {
            passwordCheckOpenedEye.isVisible = false
            passwordCheckSlashedEye.isVisible = true
            editPasswordCheck.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // email ????????? ??????
        editEmail.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) { //????????? ?????? ??? ???????????? ?????? ??? ???????????? ???????????? ??????
                val inputEmail: String = editEmail.text.toString()
                checkEmail(inputEmail)
            }
        }

        // password ????????? ??????
        editPassword.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> // editText??? ???????????? ????????? ?????? event listener
                val checkIcon: ImageView = findViewById(R.id.passwordCheckIcon)
                val xIcon: ImageView = findViewById(R.id.passwordXIcon)
                if (hasFocus) { // password editText??? ???????????? ?????? ???????????? "4?????? ??????"????????? ????????? ????????? ????????? ??????
                    val passwordRequirement: TextView = findViewById(R.id.SignUpPasswordRequirement)
                    passwordRequirement.isVisible = true
                } else {
                    if (editPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        passwordSlashedEye.isVisible = true
                    } else {
                        passwordOpenedEye.isVisible = true
                    }
                    if (editPassword.text.toString().length >= 4) {
                        if (editPasswordCheck.text.toString()
                                .isNotEmpty()
                        ) {// ???????????? ????????? ?????? ?????? ?????? ??? ?????? ??????????????? ????????? ?????? ????????? ??????????????? ?????? ????????? ?????????????????? ???.
                            val passwordCorrect: TextView = findViewById(R.id.signUpPasswordCorrect)
                            val passwordIncorrect: TextView =
                                findViewById(R.id.signUpPasswordIncorrect)

                            val checkIcon2: ImageView = findViewById(R.id.passwordCheckCheckIcon)
                            val xIcon2: ImageView = findViewById(R.id.passwordCheckXIcon)
                            if (editPassword.text.toString() == editPasswordCheck.text.toString()) {

                                passwordIncorrect.isVisible = false
                                passwordCorrect.isVisible = true

                                checkIcon2.isVisible = true
                                xIcon2.isVisible = false

                                passwordCheckFlag = true
                                activateButton()
                            } else {
                                passwordCorrect.isVisible = false
                                passwordIncorrect.isVisible = true

                                xIcon2.isVisible = true
                                checkIcon2.isVisible = false

                                passwordCheckFlag = false
                                inactivateButton()
                            }
                        }

                        checkIcon.isVisible = true
                        xIcon.isVisible = false

                        passwordFlag = true
                        activateButton()
                    } else {
                        xIcon.isVisible = true
                        checkIcon.isVisible = false

                        passwordFlag = false
                        inactivateButton()
                    }
                }
            }

        // password ????????? ??????
        editPasswordCheck.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            val passwordCorrect: TextView = findViewById(R.id.signUpPasswordCorrect)
            val passwordIncorrect: TextView = findViewById(R.id.signUpPasswordIncorrect)

            val checkIcon: ImageView = findViewById(R.id.passwordCheckCheckIcon)
            val xIcon: ImageView = findViewById(R.id.passwordCheckXIcon)
            if (!hasFocus) {
                if (editPasswordCheck.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    passwordCheckSlashedEye.isVisible = true
                } else {
                    passwordCheckOpenedEye.isVisible = true
                }
                if (editPassword.text.toString() == editPasswordCheck.text.toString()) {
                    passwordIncorrect.isVisible = false
                    passwordCorrect.isVisible = true

                    checkIcon.isVisible = true
                    xIcon.isVisible = false

                    passwordCheckFlag = true
                    activateButton()
                } else {
                    passwordCorrect.isVisible = false
                    passwordIncorrect.isVisible = true

                    xIcon.isVisible = true
                    checkIcon.isVisible = false

                    passwordCheckFlag = false
                    inactivateButton()
                }
            }
        }
    }

    private fun activateButton() { // ?????? ????????? ?????? ????????? ?????? ??? ?????? ????????? ????????? ????????????.
        if (emailFlag && passwordFlag && passwordCheckFlag) {
            nextButton.background =
                this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            nextButton.setOnClickListener {
                val intent = Intent(this, TermsActivity::class.java)
                intent.putExtra("email", editEmail.text.toString())
                intent.putExtra("password", editPassword.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun inactivateButton() { // ????????? ????????? ??? ??? ????????? ?????? ???????????? ????????? ??? ???????????? ??????
        nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        nextButton.setOnClickListener {}
    }

    private fun checkEmail(email: String) {
        val checkIcon: ImageView = findViewById(R.id.emailCheckIcon)
        val xIcon: ImageView = findViewById(R.id.emailXIcon)
        val wrongEmail: TextView = findViewById(R.id.wrongEmail)
        val unavailableEmail: TextView = findViewById(R.id.alreadyExist)

        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("email", email).build()

        val request: Request =
            Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/check").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "????????? ????????? ??????????????????. ?????? ??????????????????.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                object : Thread() {
                    override fun run() {
                        if (response.code() == 200) {
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                runOnUiThread {
                                    checkIcon.isVisible = true
                                    xIcon.isVisible = false
                                    wrongEmail.isVisible = false
                                    unavailableEmail.isVisible = false

                                    emailFlag = true
                                    activateButton()
                                }
                            } else {
                                runOnUiThread {
                                    xIcon.isVisible = true
                                    wrongEmail.isVisible = true // "???????????? ?????? ????????? ???????????????." ????????? ??????
                                    checkIcon.isVisible = false
                                    unavailableEmail.isVisible = false

                                    emailFlag = false
                                    inactivateButton()
                                }
                            }
                        } else if (response.code() == 201) {
                            runOnUiThread {
                                xIcon.isVisible = true
                                unavailableEmail.isVisible = true // "?????? ???????????? ?????? ??????????????????." ????????? ??????
                                checkIcon.isVisible = false
                                wrongEmail.isVisible = false

                                emailFlag = false
                                inactivateButton()
                            }
                        }
                    }
                }.run()
            }
        })
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