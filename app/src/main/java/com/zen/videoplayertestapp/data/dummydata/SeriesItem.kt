package com.zen.videoplayertestapp.data.dummydata

data class SeriesItem(
    val series_id: String,
    val access_type: String,
    val episodes: List<Episode>,
    val thumb_image_url: String,
    val title: String,
    val type: String,
)

data class Episode(
    val episode_id: String,
    val languages_info: String= "",
    val media_file_url:String,
    val series_ui_type: String,
    val thumb_image_url: String="",
    val title: String,
    val description: String,
    val total_shares: Int,
    val total_views: Int,
    val total_likes: Int,
    val total_comments: Int,
    val type: String,
    val video_duration: String,
    val auther_name: String,
)