package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.zen.videoplayertestapp.R

class SeriesCollectionAdapter(val context: Context,val listener: RecyclerviewClickListener):RecyclerView.Adapter<SeriesCollectionAdapter.SeriesCollectionViewholder>() {
    var data:MutableList<Series> = mutableListOf<Series>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeriesCollectionViewholder {
        val layout = LayoutInflater.from(context)
            .inflate(R.layout.series_video_vertical_item_view, parent, false)
        return SeriesCollectionViewholder(layout)
    }

    override fun onBindViewHolder(
        holder: SeriesCollectionViewholder,
        position: Int
    ) {
        val dataItem = data[position]
        if (dataItem is Series){
            Glide.with(context)
                .load(dataItem.vertical_thumb_bs2_url) // Replace `imageUrl` with your image URL property
                .into(holder.imageThumb)
        }
        holder.imageThumb.setOnClickListener {
            listener.onSeriesItemClick(dataItem,position,data)
        }
        // Load image using Glide


    }

    override fun getItemCount(): Int {
      return data.size
    }
    class SeriesCollectionViewholder(view:View):RecyclerView.ViewHolder(view){
        val imageThumb = view.findViewById<AppCompatImageView>(R.id.image)

    }
    fun updateItem(item: MutableList<Series>?){
        if (item != null) {
            data.addAll(item)
        }

    }




}