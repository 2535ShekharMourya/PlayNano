package com.zen.videoplayertestapp.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.zen.videoplayertestapp.R


class RecommendationVerticalItemsAdapter(val context: Context, val listener: RecyclerviewClickListener) : RecyclerView.Adapter<RecommendationVerticalItemsAdapter.RecommendationVerticalItemsViewHolder>() {

    private var data = mutableListOf<Series>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationVerticalItemsViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.series_video_vertical_item_view, parent, false)
        return RecommendationVerticalItemsViewHolder(layout)

    }
    override fun onBindViewHolder(holder: RecommendationVerticalItemsViewHolder, position: Int) {
        val dataItem = data[position]
        // Load image using Glide
        Glide.with(context)
            .load(dataItem.vertical_thumb_bs2_url) // Replace `imageUrl` with your image URL property
            .into(holder.imageThumb)
        holder.itemView.setOnClickListener {
        }
        holder.imageThumb.setOnClickListener {
            "imageThumb: setOnClickListener".logs()
            listener.onSeriesItemClick(dataItem,position,data)
        }
    }

    override fun getItemCount(): Int {
        return data.size

    }
    class RecommendationVerticalItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageThumb = view.findViewById<AppCompatImageView>(R.id.image)

    }

    fun additems(quizItems: List<Series>) {
        //  hideLoader()
        data.addAll(quizItems)
        notifyItemRangeInserted(itemCount - quizItems.size, quizItems.size)
    }
    fun updateItems(itemList:List<Series>){
        data.clear()
        data.addAll(itemList)
        notifyDataSetChanged()

    }


}

