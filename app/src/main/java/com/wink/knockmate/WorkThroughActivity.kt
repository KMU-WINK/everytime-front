package com.wink.knockmate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_work_through.*


class WorkThroughActivity : AppCompatActivity() {
    inner class WorkThroughAdapter(private val context: Context) :
        RecyclerView.Adapter<WorkThroughAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): WorkThroughAdapter.ViewHolder {
            val view =
                LayoutInflater.from(context).inflate(R.layout.layout_work_through, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = 4

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.translationX =
                (-resources.displayMetrics.widthPixels * position).toFloat()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_through)

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(work_through_body_recycler)
        work_through_body_recycler.adapter = WorkThroughAdapter(this)

        var isStart = false
        work_through_body_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val view = snapHelper.findSnapView(recyclerView.layoutManager)!!
                    val position = recyclerView.layoutManager!!.getPosition(view)

                    work_through_footer_img.setImageDrawable(
                        resources.getDrawable(
                            arrayListOf<Int>(
                                R.drawable.ic_work_through_1p_footer,
                                R.drawable.ic_work_through_footer_img1,
                                R.drawable.ic_work_through_footer_img2,
                                R.drawable.ic_work_through_footer_img3,
                            )[position]
                        )
                    )
                    if (position == 3) {
                        btWork_through_footer.background =
                            resources.getDrawable(R.drawable.ic_work_through_footer_bt2)
                        isStart = true
                    } else {
                        btWork_through_footer.background =
                            resources.getDrawable(R.drawable.ic_work_through_footer_bt)
                        isStart = false
                    }
                }
            }
        })
        btWork_through_footer.setOnClickListener {
            if (!isStart) {
                work_through_body_recycler.smoothScrollToPosition(3)
                btWork_through_footer.background =
                    resources.getDrawable(R.drawable.ic_work_through_footer_bt2)
                resources.getDrawable(
                    arrayListOf<Int>(
                        R.drawable.ic_work_through_1p_footer,
                        R.drawable.ic_work_through_footer_img1,
                        R.drawable.ic_work_through_footer_img2,
                        R.drawable.ic_work_through_footer_img3,
                    )[3]
                )
                isStart = true
            } else {
                startActivity(Intent(this, CharacterChoiceActivity::class.java))
                finish()
            }
        }
    }
}