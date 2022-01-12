package com.wink.knockmate

import java.util.*

class AddScheduleInfo {
    companion object{
        private var startCal : Calendar = Calendar.getInstance()
        private var endCal : Calendar = Calendar.getInstance()
        private var title : String = ""
        private var memo : String = ""
        private var inviteMembers = Array(10, {item->""})
        private var startDay : String = ""
        private var endDay : String = ""


        fun resetStartCal(){
            startCal = Calendar.getInstance()
            endCal = Calendar.getInstance()
            if(startCal.get(Calendar.MINUTE) > 55){
                startCal.add(Calendar.HOUR, 1)
                endCal.add(Calendar.HOUR, 2)
                startCal.set(Calendar.MINUTE, 0)
                endCal.add(Calendar.MINUTE, 0)
            }else if(startCal.get(Calendar.MINUTE)%5 != 0){
                startCal.set(Calendar.MINUTE, startCal.get(Calendar.MINUTE)-(startCal.get(Calendar.MINUTE)%5) + 5)
                endCal.add(Calendar.HOUR, 1)
                endCal.set(Calendar.MINUTE, endCal.get(Calendar.MINUTE)-(endCal.get(Calendar.MINUTE)%5) + 5)
            }else{
                endCal.add(Calendar.HOUR, 1)
            }
        }

        fun getStartCal() : Calendar{
            return startCal
        }

        fun setStartCal(dateTime:Calendar) {
            startCal = dateTime
            if(getIgnoredSecond(endCal.timeInMillis) - getIgnoredSecond(startCal.timeInMillis) > 0){
                endCal = startCal
                endCal.add(Calendar.HOUR, 1)
            }
        }

        fun getEndCal() : Calendar{
            return endCal
        }

        fun setEndCal(dateTime: Calendar){
            endCal = dateTime
        }

        fun getTitle() : String{
            return title
        }

        fun setTitle(title:String){
            this.title = title
        }

        fun getMemo() : String{
            return memo
        }

        fun setMemo(memo:String){
            this.memo = memo
        }

        fun getInviteMembers() : Array<String>{
            return inviteMembers
        }

        fun setInviteMembers(inviteMembers:Array<String>){
            this.inviteMembers = inviteMembers
        }

        fun getStartDay() : String{
            return startDay
        }

        fun setStartDay(day:String){
            startDay = day
        }

        fun getEndDay() : String{
            return endDay
        }

        fun setEndDay(day:String){
            endDay = day
        }

        private fun getIgnoredSecond(time:Long) : Long{
            return Calendar.getInstance().apply {
                timeInMillis = time

                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }
    }
}