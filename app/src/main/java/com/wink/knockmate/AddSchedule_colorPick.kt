package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class AddSchedule_colorPick : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_colorpick, container, false)

        val tempColor = AddScheduleInfo.color

        val color1Check = view.findViewById<CheckBox>(R.id.color1_check)
        val color2Check = view.findViewById<CheckBox>(R.id.color2_check)
        val color3Check = view.findViewById<CheckBox>(R.id.color3_check)
        val color4Check = view.findViewById<CheckBox>(R.id.color4_check)
        val color5Check = view.findViewById<CheckBox>(R.id.color5_check)
        val color6Check = view.findViewById<CheckBox>(R.id.color6_check)
        val color7Check = view.findViewById<CheckBox>(R.id.color7_check)
        val color8Check = view.findViewById<CheckBox>(R.id.color8_check)
        val color9Check = view.findViewById<CheckBox>(R.id.color9_check)
        val color10Check = view.findViewById<CheckBox>(R.id.color10_check)
        val color11Check = view.findViewById<CheckBox>(R.id.color11_check)
        val color12Check = view.findViewById<CheckBox>(R.id.color12_check)

        val color1 = view.findViewById<ConstraintLayout>(R.id.color1)
        val color2 = view.findViewById<ConstraintLayout>(R.id.color2)
        val color3 = view.findViewById<ConstraintLayout>(R.id.color3)
        val color4 = view.findViewById<ConstraintLayout>(R.id.color4)
        val color5 = view.findViewById<ConstraintLayout>(R.id.color5)
        val color6 = view.findViewById<ConstraintLayout>(R.id.color6)
        val color7 = view.findViewById<ConstraintLayout>(R.id.color7)
        val color8 = view.findViewById<ConstraintLayout>(R.id.color8)
        val color9 = view.findViewById<ConstraintLayout>(R.id.color9)
        val color10 = view.findViewById<ConstraintLayout>(R.id.color10)
        val color11 = view.findViewById<ConstraintLayout>(R.id.color11)
        val color12 = view.findViewById<ConstraintLayout>(R.id.color12)

        val icon = view.findViewById<ImageView>(R.id.yellow_icon)


        if (AddScheduleInfo.color == 0) {
            color1Check.isChecked = true
            Glide.with(this).load(R.drawable.color1).into(icon)
        } else if (AddScheduleInfo.color == 1) {
            color2Check.isChecked = true
            Glide.with(this).load(R.drawable.color2).into(icon)
        } else if (AddScheduleInfo.color == 2) {
            color3Check.isChecked = true
            Glide.with(this).load(R.drawable.color3).into(icon)
        } else if (AddScheduleInfo.color == 3) {
            color4Check.isChecked = true
            Glide.with(this).load(R.drawable.color4).into(icon)
        } else if (AddScheduleInfo.color == 4) {
            color5Check.isChecked = true
            Glide.with(this).load(R.drawable.color5).into(icon)
        } else if (AddScheduleInfo.color == 5) {
            color6Check.isChecked = true
            Glide.with(this).load(R.drawable.color6).into(icon)
        } else if (AddScheduleInfo.color == 6) {
            color7Check.isChecked = true
            Glide.with(this).load(R.drawable.color7).into(icon)
        } else if (AddScheduleInfo.color == 7) {
            color8Check.isChecked = true
            Glide.with(this).load(R.drawable.color8).into(icon)
        } else if (AddScheduleInfo.color == 8) {
            color9Check.isChecked = true
            Glide.with(this).load(R.drawable.color9).into(icon)
        } else if (AddScheduleInfo.color == 9) {
            color10Check.isChecked = true
            Glide.with(this).load(R.drawable.color10).into(icon)
        } else if (AddScheduleInfo.color == 10) {
            color11Check.isChecked = true
            Glide.with(this).load(R.drawable.color11).into(icon)
        } else {
            color12Check.isChecked = true
            Glide.with(this).load(R.drawable.color12).into(icon)
        }

        fun initFalse() {
            color1Check.isChecked = false
            color2Check.isChecked = false
            color3Check.isChecked = false
            color4Check.isChecked = false
            color5Check.isChecked = false
            color6Check.isChecked = false
            color7Check.isChecked = false
            color8Check.isChecked = false
            color9Check.isChecked = false
            color10Check.isChecked = false
            color11Check.isChecked = false
            color12Check.isChecked = false
        }


        color1.setOnClickListener {
            AddScheduleInfo.color = 1
            initFalse()
            color1Check.isChecked = true
        }
        color2.setOnClickListener {
            AddScheduleInfo.color = 2
            initFalse()
            color2Check.isChecked = true
        }
        color3.setOnClickListener {
            AddScheduleInfo.color = 3
            initFalse()
            color3Check.isChecked = true
        }
        color4.setOnClickListener {
            AddScheduleInfo.color = 4
            initFalse()
            color4Check.isChecked = true
        }
        color5.setOnClickListener {
            AddScheduleInfo.color = 5
            initFalse()
            color5Check.isChecked = true
        }
        color6.setOnClickListener {
            AddScheduleInfo.color = 6
            initFalse()
            color6Check.isChecked = true
        }
        color7.setOnClickListener {
            AddScheduleInfo.color = 7
            initFalse()
            color7Check.isChecked = true
        }
        color8.setOnClickListener {
            AddScheduleInfo.color = 8
            initFalse()
            color8Check.isChecked = true
        }
        color9.setOnClickListener {
            AddScheduleInfo.color = 9
            initFalse()
            color9Check.isChecked = true
        }
        color10.setOnClickListener {
            AddScheduleInfo.color = 10
            initFalse()
            color10Check.isChecked = true
        }
        color11.setOnClickListener {
            AddScheduleInfo.color = 11
            initFalse()
            color11Check.isChecked = true
        }
        color12.setOnClickListener {
            AddScheduleInfo.color = 12
            initFalse()
            color12Check.isChecked = true
        }

        color1Check.setOnClickListener {
            AddScheduleInfo.color = 1
            initFalse()
            color1Check.isChecked = true
        }
        color2Check.setOnClickListener {
            AddScheduleInfo.color = 2
            initFalse()
            color2Check.isChecked = true
        }
        color3Check.setOnClickListener {
            AddScheduleInfo.color = 3
            initFalse()
            color3Check.isChecked = true
        }
        color4Check.setOnClickListener {
            AddScheduleInfo.color = 4
            initFalse()
            color4Check.isChecked = true
        }
        color5Check.setOnClickListener {
            AddScheduleInfo.color = 5
            initFalse()
            color5Check.isChecked = true
        }
        color6Check.setOnClickListener {
            AddScheduleInfo.color = 6
            initFalse()
            color6Check.isChecked = true
        }
        color7Check.setOnClickListener {
            AddScheduleInfo.color = 7
            initFalse()
            color7Check.isChecked = true
        }
        color8Check.setOnClickListener {
            AddScheduleInfo.color = 8
            initFalse()
            color8Check.isChecked = true
        }
        color9Check.setOnClickListener {
            AddScheduleInfo.color = 9
            initFalse()
            color9Check.isChecked = true
        }
        color10Check.setOnClickListener {
            AddScheduleInfo.color = 10
            initFalse()
            color10Check.isChecked = true
        }
        color11Check.setOnClickListener {
            AddScheduleInfo.color = 11
            initFalse()
            color11Check.isChecked = true
        }
        color12Check.setOnClickListener {
            AddScheduleInfo.color = 12
            initFalse()
            color12Check.isChecked = true
        }

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        val saveButton = view.findViewById<TextView>(R.id.color_save_button)

        backButton.setOnClickListener {
            AddScheduleInfo.color = tempColor
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        saveButton.setOnClickListener {
            parentFragment?.childFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.addschedule_frame, AddSchedule_detail())
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }
}