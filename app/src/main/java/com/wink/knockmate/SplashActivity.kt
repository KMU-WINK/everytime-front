package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashAnimation()
    }

    @UiThread
    private fun splashAnimation() {
        val up = AnimationUtils.loadAnimation(this, R.anim.splash_up)
        val mole = findViewById<ImageView>(R.id.splash_mole)
        mole.startAnimation(up)
        up.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@SplashActivity, WorkThroughActivity::class.java))
                finish()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }
}