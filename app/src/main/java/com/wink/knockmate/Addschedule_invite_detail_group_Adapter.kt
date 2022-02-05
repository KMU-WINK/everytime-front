package com.wink.knockmate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Addschedule_invite_detail_group_Adapter(private val context: Context):RecyclerView.Adapter<Addschedule_invite_detail_group_Adapter.ViewHolder>(){

    private var datas = mutableListOf<FollowData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.addschedule_invite_detail_itme, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val groupImage = itemView.findViewById<ImageView>(R.id.profile_image)
        private val groupName = itemView.findViewById<TextView>(R.id.profile_name)

        fun bind(item: FollowData){
            if(item.nickname != null){
                groupName.text = item.nickname
            }else{
                groupName.text = item.id
            }
            val client = OkHttpClient().newBuilder().build()
            val request : Request = Request.Builder()
                .url("http://3.35.146.57:3000/picture/${item.email}")
                .method("GET", null)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("log", "인터넷 연결 불안정")
                }

                override fun onResponse(call: Call, response: Response) {
                    object : Thread(){
                        override fun run() {
                            if(response.code == 200){
                                Glide.with(itemView).load(JSONObject(response.body?.string()))
                                    .transform(CenterCrop(), RoundedCorners(20))
                                    .into(groupImage)
                            }else{
                                Glide.with(itemView).load(R.drawable.profile_default)
                                    .into(groupImage)
                            }
                        }
                    }.run()
                }
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}
