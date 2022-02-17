package com.wink.knockmate

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException

class SettingsActivity : AppCompatActivity() {

    private val pref by lazy{
        getSharedPreferences("loginInfo", MODE_PRIVATE)
    }
    //private val email by lazy{
    //    pref.getString("email", "")
    //} // TODO login branch와 merge하면 추가

    private val profileImage : ImageView by lazy{
        findViewById(R.id.profileImage)
    }

    private val userName : TextView by lazy{
        findViewById(R.id.userName)
    }

    private val userEmail : TextView by lazy{
        findViewById(R.id.userEmail)
    }

    private val editProfileButton : Button by lazy{
        findViewById(R.id.editProfileButton)
    }

    private val editEmailButton : Button by lazy{
        findViewById(R.id.changeEmail)
    }

    private val editPasswordButton : Button by lazy{
        findViewById(R.id.changePassword)
    }

    private val alarmSettingButton : ToggleButton by lazy{
        findViewById(R.id.alarmSettingButton)
    }

    private val searchSettingButton : ToggleButton by lazy{
        findViewById(R.id.searchSettingButton)
    }

    private val logoutButton : Button by lazy{
        findViewById(R.id.logout)
    }

    private val deleteAccountButton : Button by lazy{
        findViewById(R.id.deleteAccount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()

        editProfileButton.setOnClickListener {
            initEditProfileButton()
        }

        editEmailButton.setOnClickListener {
            initEditEmailButton()
        }

        editPasswordButton.setOnClickListener {
            initEditPasswordButton()
        }

        logoutButton.setOnClickListener {
            initLogoutButton()
        }

        deleteAccountButton.setOnClickListener {
            initDeleteAccountButton()
        }

        alarmSettingButton.setOnCheckedChangeListener { _, isChecked ->
            val email = "yoonsw0532@naver.com" // TODO 임시 테스트용
            val client = OkHttpClient()
            if(isChecked){
                // 검색 허용
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("notifable", "1")
                    .build()
                val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/notifable").put(body).build()

                client.newCall(request).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "알림 설정 도중 인터넷 연결 불안정")
                        runOnUiThread {
                            Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code() == 200){
                            Log.d("성공", "알림 허용으로 변경 성공")
                        } else{
                            Log.d("log", "알림 설정 도중 인터넷 연결 불안정")
                            runOnUiThread {
                                Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            } else{
                // 검색 제한
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("notifable", "0")
                    .build()
                val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/notifable").put(body).build()

                client.newCall(request).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "알림 설정 도중 인터넷 연결 불안정")
                        runOnUiThread {
                            Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code() == 200){
                            Log.d("성공", "알림 끄기로 변경 성공")
                        } else{
                            Log.d("log", "알림 설정 도중 인터넷 연결 불안정")
                            runOnUiThread {
                                Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }

        searchSettingButton.setOnCheckedChangeListener { _, isChecked ->
            val email = "yoonsw0532@naver.com" // TODO 임시 테스트용
            val client = OkHttpClient()
            if(isChecked){
                // 검색 허용
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("searchable", "1")
                    .build()
                val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/searchable").put(body).build()

                client.newCall(request).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "검색 설정 도중 인터넷 연결 불안정")
                        runOnUiThread {
                            Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code() == 200){
                            Log.d("성공", "검색 허용으로 변경 성공")
                        } else{
                            Log.d("log", "검색 설정 도중 인터넷 연결 불안정")
                            runOnUiThread {
                                Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            } else{
                // 검색 제한
                val body = FormBody.Builder()
                    .add("email", email)
                    .add("searchable", "0")
                    .build()
                val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/searchable").put(body).build()

                client.newCall(request).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "검색 설정 도중 인터넷 연결 불안정")
                        runOnUiThread {
                            Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code() == 200){
                            Log.d("성공", "검색 제한으로 변경 성공")
                        } else{
                            Log.d("log", "검색 설정 도중 인터넷 연결 불안정")
                            runOnUiThread {
                                Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun initViews(){
        var email = "yoonsw0532@naver.com" // TODO 임시 테스트용
        val client = OkHttpClient()
        val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/picture/${email}").build()

        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "프로필 이미지 정보 수신 도중 인터넷 연결 불안정")
                runOnUiThread {
                    Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.code() == 200){
                    Log.d("log", "프로필 이미지 다운로드 성공")
                    Log.d("response code", response.code().toString())
                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    runOnUiThread{
                        profileImage.setImageBitmap(bitmap)
                    }
                } else{
                    Log.d("response code", response.code().toString())
                    Log.d("log", "프로필 이미지 다운로드 실패")
                }
            }
        })

        // 프로필에 닉네임, 이메일 보여주기
        // 토글 버튼 초기 상태 설정하기
        email = "yoonsw0532@naver.com" // TODO 임시 테스트용
        val userInfoRequest = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/user?query=${email}").build()

        client.newCall(userInfoRequest).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "유저 정보 수신 도중 인터넷 연결 불안정")
                runOnUiThread {
                    Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.code() == 200){
                    Log.d("log", "유저 정보 수신 성공")
                    // TODO json 빈거 예외처리
                    val jsonObject = JSONObject(response.body()?.string())
                    val jsonArray = jsonObject.getJSONArray("data")
                    val targetObject = jsonArray.getJSONObject(0)
                    runOnUiThread {
                        userName.text = targetObject.getString("nickname")
                        userEmail.text = email
                    }
                    Log.d("searchable", targetObject.getInt("searchable").toString())
                    if(targetObject.getInt("searchable") == 1){
                        runOnUiThread {
                            searchSettingButton.isChecked = true
                        }
                    }
                    Log.d("notifable", (targetObject.getInt("notifable")==1).toString())
                    if(targetObject.getInt("notifable") == 1){
                        runOnUiThread {
                            alarmSettingButton.isChecked = true
                        }
                    }
                } else{
                    Log.d("log", "유저 정보 다운로드 실패")
                }
            }
        })
    }

    private fun initEditProfileButton(){
        val intent = Intent(this,EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun initEditEmailButton(){
        val intent = Intent(this,EditEmailActivity::class.java)
        startActivity(intent)
    }

    private fun initEditPasswordButton(){
        val intent = Intent(this,EditPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun initLogoutButton(){
        val logoutDialog = Dialog(this)
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // dialog 제목 제거
        logoutDialog.setContentView(R.layout.settings_dialog)

        val title = logoutDialog.findViewById<TextView>(R.id.dialogTitle)
        val negativeButton = logoutDialog.findViewById<Button>(R.id.negativeButton)
        val positiveButton = logoutDialog.findViewById<Button>(R.id.positiveButton)

        title.text = "로그아웃 하시겠습니까?"
        negativeButton.text = "취소"
        negativeButton.setOnClickListener {
            logoutDialog.dismiss()
        }
        positiveButton.text = "로그아웃"
        positiveButton.setOnClickListener {
            // preference에 자동 로그인을 위해 저장된 이메일과 비밀번호 정보를 초기화
            val editor = pref.edit()
            editor.putString("email","")
            editor.putString("password","")
            editor.apply()

            // 로그아웃 토스트 메세지
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

            logoutDialog.dismiss()

            // 로그인 화면으로 이동
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 실행하는 액티비티 외에 모두 제거 후 새로운 태스크 생성
            startActivity(intent)
        }

        logoutDialog.show()
        logoutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        logoutDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    private fun initDeleteAccountButton(){
        val deleteAccountDialog = Dialog(this)
        deleteAccountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        deleteAccountDialog.setContentView(R.layout.settings_dialog)

        val title = deleteAccountDialog.findViewById<TextView>(R.id.dialogTitle)
        val negativeButton = deleteAccountDialog.findViewById<Button>(R.id.negativeButton)
        val positiveButton = deleteAccountDialog.findViewById<Button>(R.id.positiveButton)

        title.text = "정말 계정을 삭제하겠습니까?\n모든 정보가 완전히 삭제됩니다."
        negativeButton.text = "취소"
        negativeButton.setOnClickListener {
            deleteAccountDialog.dismiss()
        }
        positiveButton.text = "확인"
        positiveButton.setOnClickListener {
            val email = "iandr0805@gmail.com" // 임시 테스트용 이메일

            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", email)
                .build()
            val request : Request = Request.Builder().addHeader("Content-Type","application/x-www-form-urlencoded").url("http://3.35.146.57:3000/user").delete(body).build()

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")
                    runOnUiThread {
                        Toast.makeText(this@SettingsActivity,"인터넷 연결이 불안정합니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    object: Thread(){
                        override fun run(){
                            if(response.code() == 200){
                                Log.d("email", email)
                                Log.d("delete", "success")

                                // 회원 탈퇴 후 자동 로그인을 위해 저장했던 preference의 이메일, 비밀번호 정보 초기화
                                val editor = pref.edit()
                                editor.putString("email","")
                                editor.putString("password","")
                                editor.apply()

                                // 회원 탈퇴 토스트 메세지
                                runOnUiThread {
                                    Toast.makeText(this@SettingsActivity, "계정 삭제 완료", Toast.LENGTH_SHORT).show()
                                }

                                deleteAccountDialog.dismiss()

                                // 로그인 화면으로 이동
                                val intent = Intent(this@SettingsActivity, LogInActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP // 실행하는 액티비티 외에 모두 제거 후 새로운 태스크 생성
                                startActivity(intent)

                            } else if(response.code() == 201){
                                Log.d("email", email)
                                Log.d("log", "존재하지 않는 이메일")
                                runOnUiThread {
                                    Toast.makeText(this@SettingsActivity, "계정 삭제에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            } else{
                                Log.d("log", "회원 탈퇴 실패")
                            }
                        }
                    }.run()
                }
            })
        }

        deleteAccountDialog.show()
        deleteAccountDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteAccountDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }
}