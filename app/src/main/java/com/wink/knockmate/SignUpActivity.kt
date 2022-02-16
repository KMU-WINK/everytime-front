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
            //TODO 이전 화면으로 이동
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

        // email 유효성 검사
        editEmail.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) { //이메일 입력 후 포커스를 잃은 뒤 이메일이 유효한지 체크
                val inputEmail: String = editEmail.text.toString()
                checkEmail(inputEmail)
            }
        }

        // password 유효성 검사
        editPassword.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> // editText에 포커스가 변경될 때의 event listener
                val checkIcon: ImageView = findViewById(R.id.passwordCheckIcon)
                val xIcon: ImageView = findViewById(R.id.passwordXIcon)
                if (hasFocus) { // password editText에 포커스가 잡힌 직후부터 "4자리 이상"이라는 문구를 하단에 띄우기 위함
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
                        ) {// 비밀번호 재입력 일치 여부 확인 후 다시 비밀번호를 수정할 경우 수정된 비밀번호와 일치 여부를 재확인해줘야 함.
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

        // password 재입력 검사
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

    private fun activateButton() { // 모든 정보를 기입 했는지 확인 후 버튼 활성화 여부를 결정한다.
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

    private fun inactivateButton() { // 버튼이 활성화 된 후 버튼을 다시 비활성화 해야할 때 사용되는 함수
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
                        "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
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
                                    wrongEmail.isVisible = true // "올바르지 않은 이메일 형식입니다." 문구를 띄움
                                    checkIcon.isVisible = false
                                    unavailableEmail.isVisible = false

                                    emailFlag = false
                                    inactivateButton()
                                }
                            }
                        } else if (response.code() == 201) {
                            runOnUiThread {
                                xIcon.isVisible = true
                                unavailableEmail.isVisible = true // "이미 가입되어 있는 이메일입니다." 문구를 띄움
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