package com.wink.knockmate

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Search_Item_Adapter(private val context: Context): RecyclerView.Adapter<Search_Item_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(v: CheckBox, data: UserModel, pos: Int)
    }

    private var listener: OnCheckBoxClickListener? = null
    fun setOnCheckBoxClickListener(listener: OnCheckBoxClickListener) {
        this.listener = listener
    }
    //checkBox 외부 이벤트 리스너

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_follower_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val followerImage = itemView.findViewById<ImageView>(R.id.profile_image)
        private val followerName = itemView.findViewById<TextView>(R.id.profile_name)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.isFollow)

        fun bind(item: UserModel) {
            if (item.nickname != null) {
                followerName.text = item.nickname
            } else {
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
//                            if (response.code == 200) {
//                                Glide.with(itemView).load(JSONObject(response.body?.string()))
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

            if(item.follow){
                checkBox.isChecked = true
                checkBox.text = "Following"
                checkBox.setTextColor(Color.parseColor("#FF7A53"))
            }else{
                checkBox.isChecked = false
                checkBox.text = "Follow"
                checkBox.setTextColor(Color.WHITE)
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                checkBox.setOnClickListener {
                    listener?.onCheckBoxClick(checkBox, item, pos)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}