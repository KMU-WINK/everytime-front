package com.wink.knockmate

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Search_Item_Adapter(private val context: Context) :
    RecyclerView.Adapter<Search_Item_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(v: ImageButton, data: UserModel, pos: Int)
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
        private val followButton = itemView.findViewById<ImageButton>(R.id.search_item_follow)
        private val followView = itemView.findViewById<ImageView>(R.id.search_item_follow_back)
        private val followText = itemView.findViewById<TextView>(R.id.search_item_follow_text)


        fun bind(item: UserModel) {
            itemView.findViewById<ImageView>(R.id.to_detail).visibility = View.GONE
            followButton.visibility = View.VISIBLE
            followView.visibility = View.VISIBLE
            followText.visibility = View.VISIBLE

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

            if (item.follow > 0) {
                followText.text = "Following"
                followText.setTextColor(followText.context.resources.getColor(R.color.brand_main))
                followView.setColorFilter(followText.context.resources.getColor(R.color.white))
            } else {
                followText.text = "Follow"
                followText.setTextColor(followText.context.resources.getColor(R.color.white))
                followView.setColorFilter(followText.context.resources.getColor(R.color.brand_main))
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                followButton.setOnClickListener {
                    listener?.onCheckBoxClick(followButton, item, pos)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}