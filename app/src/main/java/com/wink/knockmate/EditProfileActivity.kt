package com.wink.knockmate

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

class EditProfileActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }

    private val userImage : ImageButton by lazy{
        findViewById(R.id.profileImage)
    }

    private val editNickname : EditText by lazy{
        findViewById(R.id.editNickname)
    }

    private val completeButton : Button by lazy{
        findViewById(R.id.completeButton)
    }

    private val nicknameShort : TextView by lazy{
        findViewById(R.id.nicknameShort)
    }

    private val nicknameLong : TextView by lazy{
        findViewById(R.id.nicknameLong)
    }

    private val nicknameWrong : TextView by lazy{
        findViewById(R.id.nicknameFormWrong)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        backButton.setOnClickListener {
            finish()
        }

        val imageFlag = false

        // TODO 서버에서 기존 프로필 사진 가져오기
        // TODO 프로필 사진 있으면 imageFlag 값을 true로 변경
        // TODO 프로필 사진 없으면 기본 프로필 사진으로

        userImage.setOnClickListener {
            // TODO 프로필 사진 있는 경우 (imageFlag 이용해서 판별) 앨범에서 선택 / 프로필 사진 삭제 선택 기능
            if(imageFlag){
                val bottomSheetView = layoutInflater.inflate(R.layout.edit_profileimage_bottom_sheet, null)
                val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheet)
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }

            // TODO 프로필 사진 없는 경우 바로 앨범으로 전환
            // TODO 선택된 사진을 userImage에 올리기
        }

        // TODO 서버에서 기존 닉네임 가져오기
        // TODO editNickname.text에 기본값으로 기존 닉네임 넣어주기

        editNickname.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                nicknameWrong.isVisible = false
                nicknameLong.isVisible = false
                nicknameShort.isVisible = false
                if(checkNickname() == "OK"){
                    activateButton()
                } else if(checkNickname() == "Wrong"){
                    inactivateButton()
                    nicknameWrong.isVisible = true
                } else if(checkNickname() == "Short"){
                    inactivateButton()
                    nicknameShort.isVisible = true
                } else{
                    inactivateButton()
                    nicknameLong.isVisible = true
                }
            }
        }
    }

    fun deleteProfileImage(v : View){
        // TODO 프로필 사진을 삭제한다.
    }

    fun selectImage(v : View){
        // TODO 앨범에서 이미지를 선택한다.
    }

    fun close(v : View){
        // TODO bottom_sheet이 꺼지도록 한다.
    }

    private fun checkNickname() : String { // 닉네임이 유효한지 검사
        val nickname = editNickname.text.toString()
        if(!isValid(nickname)){ // 닉네임 형식이 유효하지 않으면
            return "Wrong"
        } else if(nickname.length < 2){
            return "Short"
        } else if(nickname.length > 12) {
            return "Long"
        } else {
            return "OK"
        }
    }

    private fun isValid(str : String) : Boolean{ // 닉네임이 한글, 영어, 숫자로만 이루어져있는지 확인
        var i = 0
        while(i < str.length){
            val c = str.codePointAt(i)
            if(c in 0xAC00..0xD800 || c in 0x0041..0x005A || c in 0x0061..0x007A || c in 0x0030..0x0039){
                i += Character.charCount(c)
            } else {
                return false
            }
        }
        return true
    }

    private fun inactivateButton(){
        completeButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        completeButton.setOnClickListener {  }
    }

    private fun activateButton(){
        completeButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
        completeButton.setOnClickListener {
            // TODO 수정된 프로필 이미지와 닉네임을 서버로 전송
            finish()
        }
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