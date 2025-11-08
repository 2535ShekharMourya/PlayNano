package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.brochill.minismodule.CarouselModel
import com.brochill.minismodule.util.LogStatus.Companion.logs
import com.brochill.minismodule.util.LogStatus.Companion.logs1
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zen.videoplayertestapp.R


class CarouselAdapter(val context: Context?, val listener: CarouselItemListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var data = mutableListOf<Any>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layout = LayoutInflater.from(context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                if (data[position] is CarouselModel) {
                    val carouselItem = data[position] as CarouselModel
                    val holder0: CarouselViewHolder = holder as CarouselViewHolder
                    if (carouselItem.thumbImageBs2Url.isNullOrEmpty()) {
                        "pos: $position of carousel has empty image_url".logs()
                    } else {

                        //Utils.log("loading carousel imageUrl: ${carouselItem.image_url}")
                        Glide.with(holder0.carouselImageView.context)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.3f)
                            .load(carouselItem.thumbImageBs2Url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    // Set the loaded image to the ImageView
                                    holder0.carouselImageView.setImageBitmap(resource)

                                    // Generate a Palette from the image
                                    Palette.from(resource).generate { palette ->
                                        val vibrantColor = palette?.vibrantSwatch?.rgb ?: 0x000000
                                        val darkVibrantColor = palette?.darkVibrantSwatch?.rgb ?: 0x000000

                                        // Add transparency (adjust alpha channel)
                                        val vibrantColorTransparent = vibrantColor and 0x00FFFFFF or (0xFF000000.toInt())
                                        val darkVibrantColorTransparent = darkVibrantColor and 0x00FFFFFF or (0x00000000)

                                        // Create a gradient drawable
                                        val gradientDrawable = GradientDrawable(
                                            GradientDrawable.Orientation.BOTTOM_TOP ,
                                            intArrayOf(vibrantColorTransparent, darkVibrantColorTransparent)
                                        )

                                        // Set the gradient as the background of the overlay
                                        holder0.carouselGradient.background = gradientDrawable

                                    }
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // Handle cleanup
                                }
                            })


//                        Glide.with(holder0.carouselImageView.context)
//                            .load(carouselItem.thumbImageBs2Url)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .thumbnail(0.3f)
//                            .into(holder0.carouselImageView)
                    }
                    holder0.itemView.setOnClickListener {
                        if (carouselItem.type.isNullOrEmpty()) {
                            "carousel type is empty".logs()
                        } else {
                            when (carouselItem.type) {
                                "video" -> {
                                    "open video in new screen...".logs()
                                    listener.onItemClick(
                                        carouselItem,
                                        position,
                                        CarouselItemListener.ACTION_VID
                                    )

                                }
                                "series" -> {
                                    "open series in new screen...".logs1()
                                    listener.onItemClick(
                                        carouselItem,
                                        position,
                                        CarouselItemListener.ACTION_SERIES
                                    )

                                }
                            }
                        }
                        "ccarousel type:${carouselItem.type} }".logs()
                    }
                    holder0.itemView
                }

            }

        }


    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    private class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carouselImageView = view.findViewById<ImageView>(R.id.carousel_imv)
        val carouselGradient = view.findViewById<View>(R.id.gradient)
    }

    fun removeCard(pos: Int) {
        if (pos >= 0 && pos < data.size) {
            data.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }


    fun additems(quizItems: List<CarouselModel>) {
        //  hideLoader()
        data.addAll(quizItems)
        notifyItemRangeInserted(itemCount - quizItems.size, quizItems.size)
    }

    interface CarouselItemListener {
        companion object {
            const val ACTION_LINK = "link"
            const val ACTION_VID = "video"
            const val ACTION_CUS_VID = "customisable_vi"
            const val ACTION_UN_CUS_VID = "un_customisable_vi"
            const val ACTION_COLLECTION = "collections"
            const val ACTION_STUDIO = "studio_detail"
            const val ACTION_SERIES = "series"


        }

        fun onItemClick(item: Any, position: Int, action: String)
    }
}