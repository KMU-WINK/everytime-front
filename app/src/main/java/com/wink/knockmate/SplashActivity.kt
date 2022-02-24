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
        val down = AnimationUtils.loadAnimation(this, R.anim.splash_down)
        val up = AnimationUtils.loadAnimation(this, R.anim.splash_up)
        val mole = findViewById<ImageView>(R.id.splash_mole)
        mole.startAnimation(down)
        var count = 0
        down.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                if (count >= 4) {
                    startActivity(Intent(this@SplashActivity, TempActivity::class.java))
                    finish()
                    return
                }
                count += 1
                mole.startAnimation(up)
            }

            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        up.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                if (count >= 4) {
                    startActivity(Intent(this@SplashActivity, TempActivity::class.java))
                    finish()
                    return
                }
                count += 1
                mole.startAnimation(down)
            }

            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }
}