package com.wink.knockmate

import android.provider.ContactsContract
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class KnockNotationAdapter(val knockList: Array<KnockList>) :
    RecyclerView.Adapter<KnockNotationAdapter.CustomViewHolder>() {
    data class KnockList(
        val nickname: String,
        val month: Int,
        val day: Int,
        val startTime: Int,
        val startMinute: Int,
        val endTime: Int,
        val endMinute: Int,
        val comment: String
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KnockNotationAdapter.CustomViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.knock_notation_item, parent, false)
        return KnockNotationAdapter.CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: KnockNotationAdapter.CustomViewHolder, position: Int) {
        var time: String
        if (knockList.get(position).startTime > 12) time = "오후"
        else time = "오전"
        holder.nickname.text =
            Html.fromHtml(("<b>${knockList.get(position).nickname}</b>") + "님에게 노크가 도착했습니다.")
        holder.schedule.text =
            "${knockList.get(position).month}월 ${knockList.get(position).day}일 ${time} ${
                knockList.get(position).startTime
            }시 ${knockList.get(position).startMinute}분 - ${knockList.get(position).endTime}시 ${
                knockList.get(
                    position
                ).endMinute
            }분 "
        holder.comment.text = knockList.get(position).comment
    }


    override fun getItemCount(): Int {
        return knockList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname = itemView.findViewById<TextView>(R.id.textKnockNotationFrom)
        val schedule = itemView.findViewById<TextView>(R.id.textKnockNotationSchedule)
        val comment = itemView.findViewById<TextView>(R.id.textKnockNotationContents)

    }


}