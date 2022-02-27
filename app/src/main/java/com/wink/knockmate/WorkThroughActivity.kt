package com.wink.knockmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_work_through.*

class WorkThroughActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_through)

        var cnt = 0
        btWork_through_footer.setOnClickListener{
            when(cnt){
                0 -> {
                    work_through_footer_img.setImageResource(R.drawable.ic_work_through_footer_img1)
                    cnt++
                    work_through_body.scrollTo(360,0)
                }
                1 -> {
                    work_through_footer_img.setImageResource(R.drawable.ic_work_through_footer_img2)
                    cnt++
                    work_through_body.scrollTo(720,0)
                }
                2 -> {
                    work_through_footer_img.setImageResource(R.drawable.ic_work_through_footer_img3)
                    cnt++
                    work_through_body.scrollTo(1080,0)
                }
            }

        }
    }
}