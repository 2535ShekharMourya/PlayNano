package com.brochill.minismodule.data.model

data class HomepageResponse(
    var `data`: List<SeriesItem>,
    val status: String
)
data class DashboardResponse(
    val status: String?=null,
    val seriesList: List<Series>
)
data class SeriesItem(
    val access_type: String,
    val created_at: String,
    val dynamic_link: String,
    val episodes: List<Episode>,
    val is_active: Int,
    val is_archived: Int,
    val language_id: String,
    val series: List<Series>,
    val series_id: String,
    val series_ui_type: String,
    val show_title: Boolean,
    val status: String,
    val thumb_image_bs2_url: String,
    val vertical_thumb_bs2_url:String? = null,
    val title: String,
    val type: String,
    val updated_at: String
)

data class Episode(
    val access_type: String,
    val created_at: String,
    val dynamic_link: String,
    val is_active: Int,
    val is_archived: Int,
    val language_id: String,
    val languages_info: String,
    val episode_id: String,
    val media_file_url:String,
    val series_ui_type: String,
    val show_title: Boolean,
    val status: String,
    val sub_titles_info: String,
    val thumb_image_bs2_url: String,
    val vertical_thumb_bs2_url:String? = null,
    val title: String,
    val total_shares: Int,
    val total_views: Int,
    val type: String,
    val updated_at: String
)

data class Series(
    val access_type: String,
    val created_at: String,
    val dynamic_link: String,
    val is_active: Int,
    val is_archived: Int,
    val language_id: String,
    val languages_info: String,
    val series_id: String,
    val series_ui_type: String,
    val show_title: Boolean,
    val status: String,
    val sub_titles_info: String,
    val thumb_image_bs2_url: String,
    val vertical_thumb_bs2_url:String?=null,
    val title: String,
    val total_shares: Int,
    val total_views: Int,
    val type: String,
    val updated_at: String
)