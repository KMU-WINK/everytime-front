package com.wink.knockmate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Addschedule_invited_item_Adapter(private val context: Context) :
    RecyclerView.Adapter<Addschedule_invited_item_Adapter.ViewHolder>() {
    var datas = mutableListOf<UserModel>()

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(v: TextView, data: UserModel, pos: Int)
    }

    private var listener: OnDeleteButtonClickListener? = null
    fun setOnDeleteButtonClickListener(listener: OnDeleteButtonClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.addschedule_invite_invited_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = itemView.findViewById<TextView>(R.id.invited_member)
        private val x_button = itemView.findViewById<TextView>(R.id.invited_deleteButton)

        fun bind(item: UserModel) {
            if (item.nickname != null) {
                name.text = item.nickname
            } else {
                name.text = item.id
            }
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                x_button.setOnClickListener {
                    listener?.onDeleteButtonClick(x_button, item, pos)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}