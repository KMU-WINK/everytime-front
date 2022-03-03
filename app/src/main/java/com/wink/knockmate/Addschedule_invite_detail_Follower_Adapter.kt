package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Addschedule_invite_detail_Follower_Adapter :
    RecyclerView.Adapter<Addschedule_invite_detail_Follower_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnCheckBoxClickListener {
        fun onCheckClick(v: CheckBox, data: UserModel, pos: Int)
    }

    private var listener: OnCheckBoxClickListener? = null
    fun setOnCheckBoxClickListener(listener: OnCheckBoxClickListener) {
        this.listener = listener
    }

    interface OnArrowClickListener {
        fun onArrowClick(v: ImageView, data: UserModel, pos: Int)
    }

    private var listener2: OnArrowClickListener? = null
    fun setOnArrowClickListener(listener: OnArrowClickListener) {
        this.listener2 = listener2
    }

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

        @SuppressLint("SetTextI18n")
        fun bind(item: UserModel) {
            if (item.user) {
                arrow.visibility = View.GONE
            } else {
                arrow.visibility = View.VISIBLE
            }
            if (item.nickname != null && !item.user) {
                followerName.text = item.nickname + " (" + item.isFav + "명)"
            } else if (item.nickname == null && !item.user) {
                followerName.text = item.id + " (" + item.isFav + "명)"
            }
            if (item.nickname != null && item.user) {
                followerName.text = item.nickname
            } else if (item.nickname == null && item.user) {
                followerName.text = item.id
            }

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


            checkbox.isChecked = item.invite
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                checkbox.setOnClickListener {
                    listener?.onCheckClick(checkbox, item, pos)
                }
                arrow.setOnClickListener {
                    listener2?.onArrowClick(arrow, item, pos)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }


}
