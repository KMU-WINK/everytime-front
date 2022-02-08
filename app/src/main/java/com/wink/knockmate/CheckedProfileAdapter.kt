package com.wink.knockmate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckedProfileAdapter(private val dataset: Array<ProfileInviteAdapter.ProfileData>) : RecyclerView.Adapter<CheckedProfileAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checked_profile_item,parent,false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckedProfileAdapter.CustomViewHolder, position: Int) {
        holder.btName.text=dataset[position].ProName
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
    inner class CustomViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val btName = itemView.findViewById<TextView>(R.id.rv_checked_name)
        init {
            itemView.findViewById<Button>(R.id.rv_bt_remove).setOnClickListener {
                Log.d("dd","${dataset.size}")
            }
        }

    }
}