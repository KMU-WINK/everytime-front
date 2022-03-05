package com.wink.knockmate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Addschedule_invited_gray_item_Adapter(private val context: Context) :
    RecyclerView.Adapter<Addschedule_invited_gray_item_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.addschedule_invite_invited_gray_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = itemView.findViewById<TextView>(R.id.invited_member)

        fun bind(item: UserModel) {
            if (item.nickname != null) {
                name.text = item.nickname
            } else {
                name.text = item.id
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}