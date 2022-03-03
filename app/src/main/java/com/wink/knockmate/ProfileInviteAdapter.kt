package com.wink.knockmate

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ProfileInviteAdapter(private val dataset: MutableList<ProfileInviteAdapter.ProfileData>) :
    RecyclerView.Adapter<ProfileInviteAdapter.CustomViewHolder>(), Filterable {

    private var filters: MutableList<ProfileInviteAdapter.ProfileData> = dataset

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(pos: Int)
    }

    private var listener: OnCheckBoxClickListener? = null
    fun setOnCheckBoxClickListener(listener: OnCheckBoxClickListener) {
        this.listener = listener
    }

    class ProfileData(ProName: String, ProEmail: String, ProImage: Uri, isChecked: Boolean) {
        val ProName: String = ProName
        val ProEmail: String = ProEmail
        val ProImage: Uri = ProImage
        var isChecked: Boolean = isChecked
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileInviteAdapter.CustomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.invite_profile_item, parent, false)
        return CustomViewHolder(view)

    }

    override fun onBindViewHolder(holder: ProfileInviteAdapter.CustomViewHolder, position: Int) {
        holder.proName.text = filters[position].ProName
        holder.proImage.setImageURI(filters[position].ProImage)
        holder.proCheck.setBackgroundResource(if (filters[position].isChecked) R.drawable.true_radio else R.drawable.false_radio)
        val c = holder.proCheck
        val pos: Int = position
        holder.itemView.setOnClickListener {
            filters[pos].isChecked = if (filters[pos].isChecked) {
                c.setBackgroundResource(R.drawable.false_radio)
                false
            } else {
                c.setBackgroundResource(R.drawable.true_radio)
                true
            }
            listener?.onCheckBoxClick(pos)
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val proName = itemView.findViewById<TextView>(R.id.recycleProfileName)
        val proImage = itemView.findViewById<ImageView>(R.id.recycleProfilePic)
        val proCheck = itemView.findViewById<ImageButton>(R.id.btRecycleProfileCheck)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filters = if (charString.isEmpty()) {
                    dataset
                } else {
                    val filteredList = mutableListOf<ProfileData>()
                    for (data in dataset) {
                        if (data.ProName.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault()))
                        ) {
                            filteredList.add(data);
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filters
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filters = results.values as MutableList<ProfileData>
                notifyDataSetChanged()
            }
        }
    }
}