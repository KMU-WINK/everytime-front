package com.wink.knockmate

import android.app.backup.SharedPreferencesBackupHelper
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
class ProfileInviteAdapter(private val dataset:MutableList<ProfileInviteAdapter.ProfileData>) : RecyclerView.Adapter<ProfileInviteAdapter.CustomViewHolder>() {
    class ProfileData(ProName:String,ProImage: Uri) {
        val ProName:String = ProName
        val ProImage:Uri = ProImage

    }
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileInviteAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invite_profile_item,parent,false)
        return CustomViewHolder(view)

    }
    override fun onBindViewHolder(holder: ProfileInviteAdapter.CustomViewHolder, position: Int) {
        holder.proName.text = dataset[position].ProName
        holder.proImage.setImageURI(dataset[position].ProImage)
    }
    override fun getItemCount(): Int {
        return dataset.size
    }

    class CustomViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val proName = itemView.findViewById<TextView>(R.id.recycleProfileName)
        val proImage = itemView.findViewById<ImageView>(R.id.recycleProfilePic)


        init{
            var btBool = false
            itemView.findViewById<ImageButton>(R.id.btRecycleProfileCheck).setOnClickListener {
                val position:Int = adapterPosition
                var btCheckItem= it.findViewById<ImageButton>(R.id.btRecycleProfileCheck)
                if(btBool){
                    btCheckItem.setBackgroundResource(R.drawable.false_radio)
                    btBool = false

                }
                else{
                    btCheckItem.setBackgroundResource(R.drawable.true_radio)
                    btBool=true
                }
                val checkedUser =itemView.findViewById<TextView>(R.id.recycleProfileName).text.toString()


            }
        }
    }
}