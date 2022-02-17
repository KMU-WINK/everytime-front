package com.wink.knockmate

import android.graphics.Color.rgb
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notation.*
import kotlinx.android.synthetic.main.nock_notation_item.*

class NotationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notation)
        val followList = arrayOf(
            FollowInfoAdapter.FollowList("shinba"),
            FollowInfoAdapter.FollowList("sangwon"),
            FollowInfoAdapter.FollowList("doyeop"),
            FollowInfoAdapter.FollowList("hyeonji")

        )

        val knockList = arrayOf(
            KnockNotationAdapter.KnockList("shinba",3,3,15,30,16,30,"놀자"),
            KnockNotationAdapter.KnockList("shinba",3,5,16,30,18,30,"팀플"),
            KnockNotationAdapter.KnockList("shinba",3,10,16,30,18,30,"팀플"),
            KnockNotationAdapter.KnockList("shinba",3,19,16,30,18,30,"팀플")
        )
        recyclerKnockNotation.layoutManager=LinearLayoutManager(this)
        recyclerKnockNotation.adapter = KnockNotationAdapter(knockList)
        recyclerKnockNotation.visibility = View.GONE

        recyclerFollowNotation.layoutManager = LinearLayoutManager(this)
        recyclerFollowNotation.adapter=FollowInfoAdapter(followList)


        btNotationKnock.setOnClickListener {
            btNotationKnock.setTextColor(rgb(255, 122, 83))
            btNotationFollow.setTextColor(rgb(161, 161, 161))
            btNotationFollowUnder.setBackgroundColor(rgb(255, 255, 255))
            btNotationKnockUnder.setBackgroundColor(rgb(255, 122, 83))
            recyclerFollowNotation.visibility= View.GONE
            recyclerKnockNotation.visibility=View.VISIBLE

        }
        btNotationFollow.setOnClickListener {
            btNotationKnock.setTextColor(rgb(161, 161, 161))
            btNotationFollow.setTextColor(rgb(255, 122, 83))
            btNotationFollowUnder.setBackgroundColor(rgb(255, 122, 83))
            btNotationKnockUnder.setBackgroundColor(rgb(255, 255, 255))
            recyclerFollowNotation.visibility=View.VISIBLE
            recyclerKnockNotation.visibility=View.GONE

        }
    }


}