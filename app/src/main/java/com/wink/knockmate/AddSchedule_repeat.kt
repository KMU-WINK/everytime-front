package com.wink.knockmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class AddSchedule_repeat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.addschedule_repeatpick, container, false)

        val radioGroup = view.findViewById<RadioGroup>(R.id.repeat_radioGroup)
        radioGroup.setOnCheckedChangeListener{ group, checkedId->
            when(checkedId){
                R.id.repeat_days_pick->{}
            }
        }
        return view
    }
}