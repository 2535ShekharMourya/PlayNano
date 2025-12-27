package com.zen.videoplayertestapp.ui.adapter

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_minis_short_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        if (shouldIUseExoplayer()) {
            holder.playerView.setOnTouchListener(object : DoubleClickListener() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onDoubleClick() {
                }

                override fun onSingleClick() {
                    holder.player?.let { player ->
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
        else {
            holder.videoViewPlayer.setOnTouchListener(object : DoubleClickListener() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onDoubleClick() {
                }

                override fun onSingleClick() {
                    if (holder.videoViewPlayer.isPlaying) {
                        holder.videoViewPlayer.pause()
                        holder.pauseVideoIv.visibility = View.VISIBLE
                    } else if (!holder.videoViewPlayer.isPlaying) {
                        holder.videoViewPlayer.start()
                        holder.pauseVideoIv.visibility = View.GONE
                    }
                }
                override fun onSwipRight() {}
                override fun onSwipLeft() {}
            })
            holder.pauseVideoIv.setOnClickListener {
                if (holder.videoViewPlayer.isPlaying) {
                    holder.videoViewPlayer.pause()
                    holder.pauseVideoIv.visibility = View.VISIBLE
                } else if (!holder.videoViewPlayer.isPlaying) {
                    holder.videoViewPlayer.start()
                    holder.pauseVideoIv.visibility = View.GONE
                }
            }
        }

        holder.setVideoItem(videos[position])


    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.player != null) {
            holder.player?.playWhenReady = false
        }

    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }

    override fun getItemCount() = videos.size

    fun addItem(video: List<Episode>) {
        this.videos.addAll(video)
        notifyItemRangeInserted(itemCount - video.size, video.size)
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoViewPlayer: VideoView = itemView.findViewById(R.id.videoPlayer_view)
        var videoVideoParent: ConstraintLayout = itemView.findViewById(R.id.videoVideoParent)
        var videoViewProgressBar: ProgressBar = itemView.findViewById(R.id.videoViewProgressBar)
        val playerView: PlayerView = itemView.findViewById(R.id.exoPlayer_view)
        val pauseButton: ImageView = itemView.findViewById(R.id.back_iv)
        val backIv: ImageView = itemView.findViewById(R.id.back_iv1)
        val pauseVideoIv: ImageView = itemView.findViewById(R.id.pause_one)
        val title: TextView = itemView.findViewById(R.id.quizitem_title)
        val title1: TextView = itemView.findViewById(R.id.quizitem_title1)
        var player: ExoPlayer? = null


        fun setVideoItem(video: Episode) {
            if (shouldIUseExoplayer()) {
                title.text = video.title
                pauseButton.setOnClickListener {
                    onBackPressed.backClick()
                }
                // Show ExoPlayer UI, hide VideoView UI
                playerView.visibility = View.VISIBLE
                videoVideoParent.visibility = View.GONE
                videoViewProgressBar.visibility = View.GONE  // Exo will manage buffering indicators
            } else {
                title1.text = video.title
                backIv.setOnClickListener {
                    onBackPressed.backClick()
                }
                // Show VideoView UI
                playerView.visibility = View.GONE
                videoVideoParent.visibility = View.VISIBLE
                videoViewProgressBar.visibility = View.VISIBLE
            }
            if (player == null) {
                initializePlayer(video)
            }
        }

        fun pauseVideo() {
            if (shouldIUseExoplayer()) {
                if (player != null) {
                    player?.playWhenReady = false
                }
            } else {
                videoViewPlayer.pause()
            }


        }

        fun playVideo() {
            if (shouldIUseExoplayer()) {
                if (player != null) {
                    player!!.playWhenReady = true
                    if (player!!.playWhenReady) {
                        pauseVideoIv.visibility = View.GONE
                    } else {
                        pauseVideoIv.visibility = View.VISIBLE
                    }
                }
            } else {
                videoViewPlayer.start()
            }


        }

        private fun initializePlayer(video: Episode) {
            if (shouldIUseExoplayer()) {
                player = ExoPlayer.Builder(context).build().apply {
                    playerView.player = this
                    val mediaItem = MediaItem.fromUri(video.media_file_url)
                    setMediaItem(mediaItem)
                    repeatMode = Player.REPEAT_MODE_ONE
                    player?.seekTo(1)
                    prepare()
                }
                player?.addListener(object : Player.Listener {
                    @Deprecated("Deprecated in Java")
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> {
                                if (playWhenReady) {
                                    player!!.playWhenReady = false
                                    player!!.seekToDefaultPosition()
                                    playVideo()
                                    // autoplay next or restart
                                }
                            }

                            Player.STATE_IDLE -> { /* nothing loaded yet */
                            }

                            Player.STATE_BUFFERING -> { /* buffering loader */
                            }

                            Player.STATE_READY -> {
                                if (playWhenReady) {
                                    // video is playing
                                } else {
                                    // video is paused
                                }
                            }

                        }
                    }
                })
            } else {
                val uri = video.media_file_url.toUri()
                videoViewPlayer.setVideoURI(uri)
                videoViewPlayer.seekTo(1)
                videoViewPlayer.setOnPreparedListener { mediaPlayer ->
                    videoViewProgressBar.visibility = View.GONE
                    mediaPlayer.start()
                    if (mediaPlayer.isPlaying) {
                        pauseVideoIv.visibility = View.GONE
                    }
                }

// On Completion - called when video ends
                videoViewPlayer.setOnCompletionListener {
                    videoViewPlayer.pause()
                    videoViewPlayer.seekTo(1)
                    playVideo()
                }

// On Info - handles buffering, rendering start etc.
                videoViewPlayer.setOnInfoListener { mediaPlayer, what, extra ->
                    // This part handles buffering / first frame events
                    when (what) {
                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            videoViewProgressBar.visibility = View.VISIBLE
                        }

                        MediaPlayer.MEDIA_INFO_BUFFERING_END,
                        MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                            videoViewProgressBar.visibility = View.GONE
                        }
                    }

                    // This part is your Java logic converted:
                    if (mediaPlayer.isLooping) {
                        videoViewProgressBar.visibility = View.VISIBLE
                    } else if (mediaPlayer.isPlaying) {
                        videoViewProgressBar.visibility = View.GONE
                        pauseVideoIv.visibility = View.GONE
                    }

                    return@setOnInfoListener true
                }


            }

        }

        fun releasePlayer() {
            if (shouldIUseExoplayer()) {
                player?.stop()
                player?.release()
                player = null
                playerView.player = null
            } else {
                videoViewPlayer.pause()
            }

        }
    }
    // for now mocking shouldIUseExoplayer
    fun shouldIUseExoplayer(): Boolean {
        return false
    }
}