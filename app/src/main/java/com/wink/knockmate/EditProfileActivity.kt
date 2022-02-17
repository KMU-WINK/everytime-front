package com.wink.knockmate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import okhttp3.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class EditProfileActivity : AppCompatActivity(), BottomSheetFragment_settings.OnDataPassListener {

    // TODO 수정 필요
    private var imagePath: String = ""

    private var imageChanged = false

    private var imageFlag: Boolean = false

    private val pref by lazy {
        getSharedPreferences("loginInfo", MODE_PRIVATE)
    }

    //private val email by lazy{
    //    pref.getString("email", "")
    //} // login branch와 merge하면 추가

    private val email = "yoonsw0532@naver.com" // 임시 테스트용 이메일

    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }

    private val userImage: ImageButton by lazy {
        findViewById(R.id.profileImage)
    }

    private val editNickname: EditText by lazy {
        findViewById(R.id.editNickname)
    }

    private val completeButton: Button by lazy {
        findViewById(R.id.completeButton)
    }

    private val nicknameShort: TextView by lazy {
        findViewById(R.id.nicknameShort)
    }

    private val nicknameLong: TextView by lazy {
        findViewById(R.id.nicknameLong)
    }

    private val nicknameWrong: TextView by lazy {
        findViewById(R.id.nicknameFormWrong)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        backButton.setOnClickListener {
            finish()
        }

        initViews()
        activateButton()

        editNickname.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                nicknameWrong.isVisible = false
                nicknameLong.isVisible = false
                nicknameShort.isVisible = false
                if (checkNickname() == "OK") {
                    activateButton()
                } else if (checkNickname() == "Wrong") {
                    inactivateButton()
                    nicknameWrong.isVisible = true
                } else if (checkNickname() == "Short") {
                    inactivateButton()
                    nicknameShort.isVisible = true
                } else {
                    inactivateButton()
                    nicknameLong.isVisible = true
                }
            }
        }
    }

    private fun initViews() {

        // 서버에서 기존 프로필 사진 가져오기
        val client = OkHttpClient()
        val request =
            Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/picture/${email}").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "프로필 이미지 정보 수신 도중 인터넷 연결 불안정")
                runOnUiThread {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code() == 200) {
                    Log.d("log", "프로필 이미지 다운로드 성공")
                    Log.d("response code", response.code().toString())
                    // 프로필 사진 있으면 imageFlag 값을 true로 변경
                    imageFlag = true
                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    runOnUiThread {
                        userImage.setImageBitmap(bitmap)
                    }
                } else {
                    Log.d("response code", response.code().toString())
                    Log.d("log", "프로필 이미지 다운로드 실패")
                }
            }
        })

        userImage.setOnClickListener {
            // 프로필 사진 있는 경우 (imageFlag 이용해서 판별) 앨범에서 선택 / 프로필 사진 삭제 선택 기능
            if (imageFlag) {
                val bottomSheetFragment = BottomSheetFragment_settings(applicationContext)

                bottomSheetFragment.show(
                    supportFragmentManager,
                    bottomSheetFragment.tag
                ) // bottom sheet fragment를 보여준다.
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    when {
                        ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            navigatePhotos()
                        }
                        shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                            requestPermissions(
                                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                                2000
                            )
                        }
                        else -> {
                            requestPermissions(
                                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                                2000
                            )
                        }
                    }
                }
            }
        }

        // 서버에서 기존 닉네임 가져오기
        val nicknameRequest =
            Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("http://3.35.146.57:3000/searchuser?query=${email}").build()

        client.newCall(nicknameRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("log", "닉네임 정보 수신 도중 인터넷 연결 불안정")
                runOnUiThread {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code() == 200) {
                    Log.d("log", "닉네임 정보 수신 성공")
                    val jsonObject = JSONObject(response.body()?.string())
                    val jsonArray = jsonObject.getJSONArray("data")
                    val targetObject = jsonArray.getJSONObject(0)
                    runOnUiThread {
                        // editNickname.text에 기본값으로 기존 닉네임 넣어주기
                        editNickname.setText(targetObject.getString("nickname"))
                    }
                } else {
                    Log.d("log", "닉네임 정보 수신 실패")
                    runOnUiThread {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    @SuppressLint("Range")
    override fun onDataPass(data: Uri?, flag: Boolean) {
        imageFlag = flag
        userImage.setImageURI(data)
        imageChanged = true
        imagePath = getRealPathFromURI(this, data!!).toString()
    }

    fun getImageUri(inContext: Context?, inImage: Bitmap?): Uri? {
        val bytes = ByteArrayOutputStream()
        if (inImage != null) {
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        }
        val path = MediaStore.Images.Media.insertImage(inContext?.getContentResolver(), inImage, "Title" + " - " + Calendar.getInstance().getTime(), null)
        return Uri.parse(path)
    }


    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                return if ("primary".equals(type, ignoreCase = true)) {
                    (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                } else {
                    val SDcardpath = getRemovableSDCardPath(context).split("/Android".toRegex())
                        .toTypedArray()[0]
                    SDcardpath + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    fun getRemovableSDCardPath(context: Context?): String {
        val storages = ContextCompat.getExternalFilesDirs(context!!, null)
        return if (storages.size > 1 && storages[0] != null && storages[1] != null) storages[1].toString() else ""
    }


    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }


    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }


    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }


    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }


    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

/*
        when (requestCode) {
            1000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    userImage.setImageURI(selectedImageUri)
                    imageFlag = true
                    imageChanged = true
                    // TODO 수정 필요
                    imagePath = selectedImageUri.toString()
                    Log.d("이미지 변경사항 저장", getRealPathFromURI(this, selectedImageUri).toString())
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            2000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigatePhotos()
                } else {
                    Toast.makeText(this, "권한을 거절하셨습니다.", Toast.LENGTH_SHORT).show()
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

    private fun inactivateButton() {
        completeButton.background =
            this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        completeButton.setOnClickListener { }
    }

    private fun activateButton() {
        completeButton.background =
            this.resources.getDrawable(R.drawable.signupbutton_background_orange)

        completeButton.setOnClickListener {
            // 수정된 닉네임 서버로 전송
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", email)
                .add("nickname", editNickname.text.toString())
                .build()
            val nicknameSaveRequest =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/nickname").put(body).build()

            client.newCall(nicknameSaveRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "닉네임 저장 도중 인터넷 연결 불안정")
                    runOnUiThread {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code() == 200) {
                        Log.d("log", "닉네임 저장 성공")
                    } else {
                        Log.d("log", "닉네임 저장 실패")
                        runOnUiThread {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })

            if (imageChanged && imagePath != "") {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart(
                        "picture",
                        "picture.png",
                        // TODO 수정 필요
                        RequestBody.create(MediaType.parse("application/octet-stream"), File(imagePath))
                    )
                    .build()

                val request =
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .url("http://3.35.146.57:3000/upload").post(requestBody).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("log", "프로필 이미지 저장 도중 인터넷 연결 불안정")
                        runOnUiThread {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.code() == 200) {
                            Log.d("log", "프로필 이미지 저장 성공")
                        } else {
                            Log.d("log", "프로필 이미지 저장 실패")
                            runOnUiThread {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "인터넷 연결이 불안정합니다. 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
            // TODO 수정된 프로필 이미지를 서버로 전송

            //finish()
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