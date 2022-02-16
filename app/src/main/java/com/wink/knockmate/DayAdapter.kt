package com.wink.knockmate

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import java.io.IOException
import java.util.*
import okhttp3.Response

import okhttp3.OkHttpClient
import org.json.JSONObject


class DayAdapter(private val context: Context) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    var datas = mutableListOf<DateData>()
    var selected: Calendar? = null
    var mainActivity: MainActivity? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_day_recycler, parent, false)
        return ViewHolder(view)
    }

    data class DateData(
        val date: Calendar,
    )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun resetColorAll(itemView: View, date: Calendar) {
        val dateNow = Calendar.getInstance()
        for (i: Int in 0..6) {
            val view = ((itemView as ViewGroup).getChildAt(i) as ViewGroup)
            val text = (view.getChildAt(2) as TextView)
            val textDay = (view.getChildAt(1) as TextView)
            val circle = (view.getChildAt(0) as View)
            if (selected != null && selected?.get(Calendar.YEAR) == date.get(Calendar.YEAR) && selected?.get(
                    Calendar.MONTH
                ) == date.get(Calendar.MONTH) && selected?.get(Calendar.DAY_OF_MONTH) == date.get(
                    Calendar.DAY_OF_MONTH
                )
            ) {
                text.setTextColor(Color.parseColor("#000000"))
                textDay.setTextColor(Color.parseColor("#000000"))
                circle.background = context.getDrawable(R.drawable.circle_select)
                circle.visibility = View.VISIBLE
            } else if (dateNow.get(Calendar.YEAR) == date.get(Calendar.YEAR) && dateNow.get(Calendar.MONTH) == date.get(
                    Calendar.MONTH
                ) && dateNow.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
            ) {
                text.setTextColor(Color.parseColor("#FFFFFF"))
                textDay.setTextColor(Color.parseColor("#FF7A53"))
                circle.background = context.getDrawable(R.drawable.circle_today)
                circle.visibility = View.VISIBLE
            } else if (date.get(Calendar.DAY_OF_WEEK) == 1 || date.get(Calendar.DAY_OF_WEEK) == 7) {
                text.setTextColor(Color.parseColor("#A1A1A1"))
                textDay.setTextColor(Color.parseColor("#A1A1A1"))
                circle.visibility = View.INVISIBLE
            } else {
                text.setTextColor(Color.parseColor("#000000"))
                textDay.setTextColor(Color.parseColor("#000000"))
                circle.visibility = View.INVISIBLE
            }
            date.add(Calendar.DATE, 1)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: DateData) {
            val date = item.date.clone() as Calendar
            val date0 = item.date.clone() as Calendar
            resetColorAll(itemView, item.date.clone() as Calendar)
            for (i: Int in 0..6) {
                val view = ((itemView as ViewGroup).getChildAt(i) as ViewGroup)
                val text = (view.getChildAt(2) as TextView)
                text.text = (date.get(Calendar.DAY_OF_MONTH)).toString()
                val dateT = date.clone() as Calendar
                view.setOnClickListener {
                    selected = dateT.clone() as Calendar
                    val date1 = date0.clone() as Calendar
                    resetColorAll(itemView, date1)

                }
                date.add(Calendar.DATE, 1)
            }
        }
    }
}