package com.wink.knockmate

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Search: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val toMakeGroup = findViewById<ConstraintLayout>(R.id.make_group)
        val searchEdit = findViewById<EditText>(R.id.search)

        val followerFrame = findViewById<ConstraintLayout>(R.id.followers)
        val followGroupFrame = findViewById<ConstraintLayout>(R.id.followgroup)
        val searchUserFrame = findViewById<ConstraintLayout>(R.id.search_user)
        val searchGroupFrame = findViewById<ConstraintLayout>(R.id.search_group)


        searchEdit.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //검색
                val id = searchEdit.text.toString()
                if(id !== ""){
                    followGroupFrame.visibility = View.GONE
                    followerFrame.visibility = View.GONE
                    searchGroupFrame.visibility = View.VISIBLE
                    searchUserFrame.visibility = View.VISIBLE
                }else{
                    followGroupFrame.visibility = View.VISIBLE
                    followerFrame.visibility = View.VISIBLE
                    searchGroupFrame.visibility = View.GONE
                    searchUserFrame.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        toMakeGroup.setOnClickListener {
            //그룹만들기로 이동
        }
    }
}