package com.wink.knockmate

import android.text.Editable
import java.util.*

class AddScheduleInfo {
    companion object {
        var userEmail: String = ""
        var userId: String = ""
        var startCal: Calendar = Calendar.getInstance()
        var endCal: Calendar = Calendar.getInstance()
        var title: String = ""
        var memo: String = ""
        var inviteMembers = mutableListOf<UserModel>()
        var invitersNumber: Int = 0
        var startDay: String = ""
        var endDay: String = ""
        var repeatType: String = "반복 안함"
        var repeatDetailType: String = "Days"
        var repeatDays = mutableListOf<Boolean>(false, false, false, false, false, false, false)
        var repeatAllCount: Int = 1
        var repeatInterval: Int = 1
        var repeatDay: Int = 0
        var color: Int = 1
        var brief: Boolean = true
        var followerList = mutableListOf<UserModel>()

        fun resetStartCal() {
            startCal = Calendar.getInstance()
            endCal = Calendar.getInstance()
            if (startCal.get(Calendar.MINUTE) > 55) {
                startCal.add(Calendar.HOUR, 1)
                endCal.add(Calendar.HOUR, 2)
                startCal.set(Calendar.MINUTE, 0)
                endCal.add(Calendar.MINUTE, 0)
            } else if (startCal.get(Calendar.MINUTE) % 5 != 0) {
                startCal.set(
                    Calendar.MINUTE,
                    startCal.get(Calendar.MINUTE) - (startCal.get(Calendar.MINUTE) % 5) + 5
                )
                endCal.add(Calendar.HOUR, 1)
                endCal.set(
                    Calendar.MINUTE,
                    endCal.get(Calendar.MINUTE) - (endCal.get(Calendar.MINUTE) % 5) + 5
                )
            } else {
                endCal.add(Calendar.HOUR, 1)
            }
        }

        fun settingStartCal(dateTime: Calendar) {
            startCal = dateTime
            if (getIgnoredSecond(endCal.timeInMillis) - getIgnoredSecond(startCal.timeInMillis) > 0) {
                endCal = startCal
                endCal.add(Calendar.HOUR, 1)
            }
        }


        private fun getIgnoredSecond(time: Long): Long {
            return Calendar.getInstance().apply {
                timeInMillis = time

                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }

        fun reset() {
            userEmail = ""
            userId = ""
            startCal = Calendar.getInstance()
            endCal = Calendar.getInstance()
            title = ""
            memo = ""
            inviteMembers = mutableListOf<UserModel>()
            invitersNumber = 0
            startDay = ""
            endDay = ""
            repeatType = "반복 안함"
            repeatDetailType = "Days"
            repeatDays = mutableListOf<Boolean>(false, false, false, false, false, false, false)
            repeatAllCount = 1
            repeatInterval = 1
            repeatDay = 0
            color = 1
            brief = true
            followerList = mutableListOf<UserModel>()
        }
    }
}