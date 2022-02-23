package com.wink.knockmate

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class FollowInfoAdapter(val followList: Array<FollowList>) :
    RecyclerView.Adapter<FollowInfoAdapter.CustomViewHolder>() {
    data class FollowList(
        val nickname : String
    )


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowInfoAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nock_notation_item,parent,false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowInfoAdapter.CustomViewHolder, position: Int) {
        holder.nickname.text= Html.fromHtml(("<b>${followList.get(position).nickname}</b>") + "님이 노크메이트가 되고 싶어 합니다.")

    }

    override fun getItemCount(): Int {
        return followList.size
    }


    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nickname = itemView.findViewById<TextView>(R.id.textFollowNotation)


        init {
            itemView.findViewById<ImageButton>(R.id.btFollowAccept).setOnClickListener {
                itemView.findViewById<TextView>(R.id.textFollowNotation).text = Html.fromHtml(("<b>${nickname.text.split("님")[0]}</b>") + "님이 노크 메이트가 되었습니다.")
                itemView.findViewById<ImageButton>(R.id.btFollowDecline).visibility=View.GONE
                itemView.findViewById<ImageButton>(R.id.btFollowAccept).visibility=View.GONE
            }
            itemView.findViewById<ImageButton>(R.id.btFollowDecline).setOnClickListener {
                itemView.findViewById<ConstraintLayout>(R.id.rv_followList).visibility=View.GONE
                Log.d("bt","${adapterPosition}")

            }
        }
    }
}