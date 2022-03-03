package com.wink.knockmate

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Modify_invited_item_Adapter :
    RecyclerView.Adapter<Modify_invited_item_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnMoreClickListener {
        fun onMoreClick(v: ImageView, data: UserModel, pos: Int)
    }

    private var listener: OnMoreClickListener? = null
    fun setOnMoreClickListener(listener: OnMoreClickListener) {
        this.listener = listener
    }

    interface OnArrowClickListener {
        fun onArrowClick(v: ImageView, data: UserModel, pos: Int)
    }

    private var listener2: OnArrowClickListener? = null
    fun setOnArrowClickListener(listener: OnArrowClickListener) {
        this.listener2 = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.addschedule_invite_detail_invited_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val followerImage = itemView.findViewById<ImageView>(R.id.detail_profile_image)
        private val followerName = itemView.findViewById<TextView>(R.id.detail_profile_name)
        private val state = itemView.findViewById<TextView>(R.id.detail_state)
        private val more = itemView.findViewById<ImageView>(R.id.detail_user_more)
        private val arrow = itemView.findViewById<ImageView>(R.id.detail_to_group_arrow)

        @SuppressLint("SetTextI18n")
        fun bind(item: UserModel) {
            if (item.user) {
                arrow.visibility = View.GONE
                more.visibility = View.VISIBLE
            } else {
                arrow.visibility = View.VISIBLE
                more.visibility = View.GONE
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
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                more.setOnClickListener {
                    listener?.onMoreClick(more, item, pos)
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
