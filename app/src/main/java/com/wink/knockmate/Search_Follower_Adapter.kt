package com.wink.knockmate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Search_Follower_Adapter(private val context: Context): RecyclerView.Adapter<Search_Follower_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnDetailClickListener{
        fun onDetailClick(v: ImageView, data: UserModel, pos:Int)
    }
    private var listener : OnDetailClickListener? = null
    fun setOnDetailClickListener(listener: OnDetailClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_follower_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val followerImage = itemView.findViewById<ImageView>(R.id.profile_image)
        private val followerName = itemView.findViewById<TextView>(R.id.profile_name)
        private val detail = itemView.findViewById<ImageView>(R.id.to_detail)

        fun bind(item: UserModel){
            if(item.nickname != null){
                followerName.text = item.nickname
            }else{
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


            val pos = adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                detail.setOnClickListener {
                    listener?.onDetailClick(detail,item,pos)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}