package com.wink.knockmate

import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FollowInfoAdapter(val followList: ArrayList<FollowList>, val activity: AppCompatActivity) : RecyclerView.Adapter<FollowInfoAdapter.CustomViewHolder>() {

    data class FollowList(
        val nickname: String,
        val email: String
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowInfoAdapter.CustomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.nock_notation_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowInfoAdapter.CustomViewHolder, position: Int) {
        val pos = position
        holder.nickname.text =
            Html.fromHtml(("<b>${followList.get(position).nickname}</b>") + "님이 노크메이트가 되고 싶어 합니다.")
        holder.decline.setOnClickListener {
            val pref = holder.accept.context.getSharedPreferences(
                "loginInfo",
                AppCompatActivity.MODE_PRIVATE
            )

            val client = OkHttpClient().newBuilder()
                .build()

            val body = FormBody.Builder()
                .add("email", pref.getString("email", "").toString())
                .add("senderEmail", followList[position].email)
                .build()

            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/decline_follow").post(body).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")
                }

                override fun onResponse(call: Call, response1: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response1.code() == 200) {
                                followList.removeAt(pos)
                                activity.runOnUiThread {
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
        }
        holder.accept.setOnClickListener {
            val pref = holder.accept.context.getSharedPreferences(
                "loginInfo",
                AppCompatActivity.MODE_PRIVATE
            )

            val client = OkHttpClient().newBuilder()
                .build()

            val body = FormBody.Builder()
                .add("email", pref.getString("email", "").toString())
                .add("senderEmail", followList[position].email)
                .build()

            val request: Request =
                Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .url("http://3.35.146.57:3000/accept_follow").post(body).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")
                }

                override fun onResponse(call: Call, response1: Response) {
                    object : Thread() {
                        override fun run() {
                            if (response1.code() == 200) {
                                followList.removeAt(pos)
                                activity.runOnUiThread {
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }.run()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return followList.size
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname = itemView.findViewById<TextView>(R.id.textFollowNotation)
        val decline = itemView.findViewById<ImageButton>(R.id.btFollowDecline)
        val accept = itemView.findViewById<ImageButton>(R.id.btFollowAccept)

        init {
            //itemView.findViewById<ImageButton>(R.id.btFollowAccept).setOnClickListener {
            //    itemView.findViewById<TextView>(R.id.textFollowNotation).text =
            //        Html.fromHtml(("<b>${nickname.text.split("님")[0]}</b>") + "님이 노크 메이트가 되었습니다.")
            //    itemView.findViewById<ImageButton>(R.id.btFollowDecline).visibility = View.GONE
            //    itemView.findViewById<ImageButton>(R.id.btFollowAccept).visibility = View.GONE
            //}
            //itemView.findViewById<ImageButton>(R.id.btFollowDecline).setOnClickListener {


            //}
        }
    }
}