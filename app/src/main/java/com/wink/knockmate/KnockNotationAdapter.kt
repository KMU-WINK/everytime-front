package com.wink.knockmate

import android.content.Intent
import android.provider.ContactsContract
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class KnockNotationAdapter(val knockList: ArrayList<KnockList>, val activity: AppCompatActivity) :
    RecyclerView.Adapter<KnockNotationAdapter.CustomViewHolder>() {
    data class KnockList(
        val nickname: String,
        val email: String,
        val id: Int,
        val start: Calendar,
        val end: Calendar,
        val memo: String
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
        val pos = position
        var time: String
        if (knockList.get(position).start.get(Calendar.HOUR) > 12) time = "오후"
        else time = "오전"
        holder.nickname.text =
            Html.fromHtml(("<b>${knockList.get(position).nickname}</b>") + "님에게 노크가 도착했습니다.")
        holder.schedule.text =
            "${knockList.get(position).start.get(Calendar.MONTH) + 1}월 ${
                knockList.get(position).start.get(
                    Calendar.DAY_OF_MONTH
                )
            }일 ${time} ${
                knockList.get(position).start.get(Calendar.HOUR)
            }시 ${knockList.get(position).start.get(Calendar.MINUTE)}분 - ${
                knockList.get(position).end.get(
                    Calendar.HOUR
                )
            }시 ${
                knockList.get(
                    position
                ).end.get(Calendar.MINUTE)
            }분 "
        holder.comment.text = knockList.get(position).memo
        holder.detail.setOnClickListener {
            val intent = Intent(
                holder.detail.context,
                KnockmateActivity::class.java
            )
            intent.putExtra("email", knockList[pos].email)
            intent.putExtra("calendarid", knockList[pos].id)
            intent.putExtra("start", knockList[pos].start)
            intent.putExtra("memo", knockList[pos].memo)
            intent.putExtra("mode", 1)
            holder.detail.context.startActivity(
                intent
            )
        }
        val pref = holder.accept.context.getSharedPreferences(
            "loginInfo",
            AppCompatActivity.MODE_PRIVATE
        )
        holder.accept.setOnClickListener {
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", pref.getString("email", "").toString())
                .add("senderEmail", knockList.get(pos).email)
                .add("calendarid", knockList.get(pos).id.toString())
                .build()
            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/accept_knock").post(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")

                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response.code() == 200) {
                                Log.d("log", pos.toString())
                                knockList.removeAt(pos)
                                activity.runOnUiThread {
                                    notifyDataSetChanged()
                                }
                            } else {
                                Log.d("log", response.message())
                            }
                        }
                    }.run()
                }
            })
        }
        holder.decline.setOnClickListener {
            val client = OkHttpClient()
            val body = FormBody.Builder()
                .add("email", pref.getString("email", "").toString())
                .add("senderEmail", knockList.get(pos).email)
                .add("calendarid", knockList.get(pos).id.toString())
                .build()
            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/decline_knock").post(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")

                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response.code() == 200) {
                                knockList.removeAt(pos)
                                activity.runOnUiThread {
                                    notifyDataSetChanged()
                                }
                            } else {
                                Log.d("log", response.message())
                            }
                        }
                    }.run()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return knockList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname = itemView.findViewById<TextView>(R.id.textKnockNotationFrom)
        val schedule = itemView.findViewById<TextView>(R.id.textKnockNotationSchedule)
        val comment = itemView.findViewById<TextView>(R.id.textKnockNotationContents)
        val detail = itemView.findViewById<ConstraintLayout>(R.id.knock_noti_item)
        val accept = itemView.findViewById<ImageButton>(R.id.btKnockNotationAccept)
        val decline = itemView.findViewById<ImageButton>(R.id.btKnockNotationDecline)
    }
}