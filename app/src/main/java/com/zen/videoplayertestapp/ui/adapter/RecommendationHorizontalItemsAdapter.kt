package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.zen.videoplayertestapp.R

class RecommendationHorizontalItemsAdapter(val context: Context, val listener: RecyclerviewClickListener) : RecyclerView.Adapter<RecommendationHorizontalItemsAdapter.RecommendationHorizontalItemsViewHolder>() {

    private var data = mutableListOf<Episode>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationHorizontalItemsViewHolder {
        val layout = LayoutInflater.from(context)
            .inflate(R.layout.series_collection_horizontal_item_view, parent, false)
        return RecommendationHorizontalItemsViewHolder(layout)

    }

    override fun onBindViewHolder(holder: RecommendationHorizontalItemsViewHolder, position: Int) {
        val dataItem = data[position]
        // Load image using Glide
        Glide.with(context)
            .load(dataItem.thumb_image_bs2_url) // Replace `imageUrl` with your image URL property
            .into(holder.imageThumb)

        holder.itemView.setOnClickListener {
        }
        holder.imageThumb.setOnClickListener {
            listener.onItemClickListener(dataItem, position,data) // Use the new method
        }

    }

    override fun getItemCount(): Int {
        return data.size

    }

    class RecommendationHorizontalItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageThumb = view.findViewById<AppCompatImageView>(R.id.image)

    }

    fun additems(quizItems: List<Episode>) {
        //  hideLoader()
        data.addAll(quizItems)
        notifyItemRangeInserted(itemCount - quizItems.size, quizItems.size)
    }
}