package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.util.ClickListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.zen.videoplayertestapp.R
import com.zen.videoplayertestapp.core.helper.DoubleClickListener

class MinisPlayerAdapter(
    private val context: Context, private val onBackPressed:ClickListener
) : RecyclerView.Adapter<MinisPlayerAdapter.VideoViewHolder>() {
    private var videos: MutableList<Episode> = mutableListOf()
    private val holders = mutableMapOf<Int, VideoViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_minis_short_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holders[position] = holder
        holder.bind(videos[position])
        holder.playerView.setOnTouchListener(object : DoubleClickListener() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onDoubleClick() {
                // Your double click handling code here
            }
            override fun onSingleClick() {
                holder.player?.let { player ->
                    Log.d("minisLog","playing: ${player.playWhenReady}: onSingleClick")
                    player.playWhenReady = !player.playWhenReady
                    if (player.playWhenReady) {
                        holder.pauseVideoIv.visibility = View.GONE
                    } else {
                        holder.pauseVideoIv.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSwipRight() {
                TODO("Not yet implemented")
            }

            override fun onSwipLeft() {
                TODO("Not yet implemented")
            }
        })
        holder.pauseVideoIv.setOnClickListener {
            holder.player?.apply {
                playWhenReady = !playWhenReady
                holder.pauseVideoIv.visibility = if (playWhenReady) View.GONE else View.VISIBLE
            }
        }


    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holders.values.remove(holder)
        holder.releasePlayer()
    }

    override fun getItemCount() = videos.size

    fun addItem(video: List<Episode>) {
        this.videos.addAll(video)
        notifyItemRangeInserted(itemCount - video.size, video.size)
    }

    // Pause all videos except the one at the given position
    fun pauseAllExcept(position: Int) {
        holders.forEach { (pos, holder) ->
            if (pos != position) {
                holder.pauseVideo()
            }
        }
    }

    // Play video at specific position
    fun playVideoAt(position: Int) {
        holders[position]?.playVideo()
    }

    // Pause video at specific position
    fun pauseVideoAt(position: Int) {
        holders[position]?.pauseVideo()
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerView: PlayerView = itemView.findViewById(R.id.exoPlayer_view)
       val pauseButton: ImageView = itemView.findViewById(R.id.back_iv)
        val pauseVideoIv: ImageView = itemView.findViewById(R.id.pause_one)
        val title: TextView = itemView.findViewById(R.id.quizitem_title)
         var player: ExoPlayer? = null

        fun bind(video: Episode) {
            title.text = video.title
            pauseButton.setOnClickListener {
                onBackPressed.backClick()
            }
            if (player == null) {
                initializePlayer(video)
            }
        }

        fun pauseVideo() {
            player?.pause()
        }

        fun playVideo() {
            player?.play()
        }

        private fun initializePlayer(video: Episode) {
            player = ExoPlayer.Builder(context).build().apply {
                playerView.player = this
                val mediaItem = MediaItem.fromUri(video.media_file_url)
                setMediaItem(mediaItem)
                repeatMode = Player.REPEAT_MODE_ONE
                prepare()
                playWhenReady = false // Don't auto-play
            }
        }

        fun releasePlayer() {
            player?.release()
            player = null
            playerView.player = null
        }
    }
}