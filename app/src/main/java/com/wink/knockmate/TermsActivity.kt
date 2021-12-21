package com.wink.knockmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class TermsActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.backButton)
    }

    private val agreeAll : CheckBox by lazy{
        findViewById(R.id.agreeAll)
    }

    private val termsOfService : CheckBox by lazy{
        findViewById(R.id.termsOfService)
    }

    private val personalInfo : CheckBox by lazy{
        findViewById(R.id.personalInfoCollection)
    }

    private val marketingInfo : CheckBox by lazy{
        findViewById(R.id.marketingInfo)
    }

    private val nextButton : Button by lazy{
        findViewById(R.id.appCompatButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        backButton.setOnClickListener {
            finish()
        }

        agreeAll.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                termsOfService.isChecked = true
                personalInfo.isChecked = true
                marketingInfo.isChecked = true
            } else{
                termsOfService.isChecked = false
                personalInfo.isChecked = false
                marketingInfo.isChecked = false
            }
        }

        termsOfService.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                activateButton()
            } else{
                inactivateButton()
            }
        }

        personalInfo.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                activateButton()
            } else{
                inactivateButton()
            }
        }
    }

    fun activateButton(){
        if(termsOfService.isChecked && personalInfo.isChecked){
            nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_orange)
            nextButton.setOnClickListener {
                val intent : Intent = Intent(this, NicknameActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun inactivateButton(){
        nextButton.background = this.resources.getDrawable(R.drawable.signupbutton_background_gray)
        nextButton.setOnClickListener {}
    }
}