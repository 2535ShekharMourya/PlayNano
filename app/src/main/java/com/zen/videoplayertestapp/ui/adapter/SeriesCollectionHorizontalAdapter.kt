package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.brochill.minismodule.util.RecyclerviewClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zen.videoplayertestapp.R

class SeriesCollectionHorizontalAdapter(val context: Context, val listener: RecyclerviewClickListener) : RecyclerView.Adapter<SeriesCollectionHorizontalAdapter.SeriesCollectionHorizontalViewHolder>() {

    private var data = mutableListOf<Episode>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesCollectionHorizontalViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.series_collection_horizontal_item_view, parent, false)
        return SeriesCollectionHorizontalViewHolder(layout)

    }
    override fun onBindViewHolder(holder: SeriesCollectionHorizontalViewHolder, position: Int) {
        val dataItem = data[position]
        // Load image using Glide
        Glide.with(context)
            .load(dataItem.thumb_image_bs2_url) // Replace `imageUrl` with your image URL property
            .into(holder.imageThumb)

        Glide.with(context)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.3f)
            .load(dataItem.thumb_image_bs2_url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Set the loaded image to the ImageView
                    holder.imageThumb.setImageBitmap(resource)

                    // Generate a Palette from the image
                    Palette.from(resource).generate { palette ->
                        val vibrantColor = palette?.vibrantSwatch?.rgb ?: 0x000000
                        val darkVibrantColor = palette?.darkVibrantSwatch?.rgb ?: 0x000000

                        // Add transparency (adjust alpha channel)
                        val vibrantColorTransparent = vibrantColor and 0x00FFFFFF or (0x60000000.toInt())
                        val darkVibrantColorTransparent = darkVibrantColor and 0x00FFFFFF or (0x00000000)

                        // Create a gradient drawable
                        val gradientDrawable = GradientDrawable(
                            GradientDrawable.Orientation.BOTTOM_TOP ,
                            intArrayOf(vibrantColorTransparent, darkVibrantColorTransparent)
                        )

                        // Set the gradient as the background of the overlay
                        holder.gradient.background =gradientDrawable

                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle cleanup
                }
            })

        holder.itemView.setOnClickListener {
        }
        holder.imageThumb.setOnClickListener {
            "Episode Data: ${data}".logs1()
            listener.onItemClickListener(dataItem, position,data) // Use the new method
        }

    }

    override fun getItemCount(): Int {
        return data.size

    }
    class SeriesCollectionHorizontalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageThumb = view.findViewById<AppCompatImageView>(R.id.image)
        val gradient = view.findViewById<View>(R.id.hoEpisodeGradient)

    }

    fun additems(quizItems: List<Episode>) {
        //  hideLoader()
        data.addAll(quizItems)
        notifyItemRangeInserted(itemCount - quizItems.size, quizItems.size)
    }

    fun updateItem(dataList:List<Episode>){
        data.clear()
        data.addAll(dataList)
        notifyDataSetChanged()
    }

}

