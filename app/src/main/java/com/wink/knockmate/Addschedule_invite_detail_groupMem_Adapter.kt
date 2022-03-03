package com.wink.knockmate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Addschedule_invite_detail_groupMem_Adapter :
    RecyclerView.Adapter<Addschedule_invite_detail_groupMem_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.addschedule_invite_detail_itme, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val followerImage = itemView.findViewById<ImageView>(R.id.detail_profile_image)
        private val followerName = itemView.findViewById<TextView>(R.id.detail_profile_name)
        private val checkbox = itemView.findViewById<CheckBox>(R.id.detail_user_check)
        private val arrow = itemView.findViewById<ImageView>(R.id.detail_to_group_arrow)
        private val arrow2 = itemView.findViewById<ImageView>(R.id.detail_to_group_arrow2)
        private val checkedBox = itemView.findViewById<ImageView>(R.id.detail_user_checked)

        @SuppressLint("SetTextI18n")
        fun bind(item: UserModel) {
            if (item.nickname != null && item.user) {
                followerName.text = item.nickname
            } else if (item.nickname == null && item.user) {
                followerName.text = item.id
            }

            checkbox.visibility = View.GONE
            arrow.visibility = View.GONE
            arrow2.visibility = View.GONE
            checkbox.visibility = View.GONE

//            val client = OkHttpClient().newBuilder()
//                .build()
//            val request: Request = Request.Builder()
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .url("http://3.35.146.57:3000/picture/${item.email}")
//                .get()
//                .build()
//            client.newCall(request).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Log.d("log1", e.message.toString())
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    object : Thread() {
//                        override fun run() {
//                            if (response.code() == 200) {
//                                Glide.with(itemView).load(JSONObject(response.body()?.string()))
//                                    .transform(CenterCrop(), RoundedCorners(20))
//                                    .into(followerImage)
//                            } else {
//                                Glide.with(itemView).load(R.drawable.profile_default)
//                                    .into(followerImage)
//                            }
//                        }
//                    }.run()
//                }
//            })

            Glide.with(itemView).load(R.drawable.profile_default)
                .into(followerImage)

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}
