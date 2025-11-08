package com.brochill.minismodule.data.model

data class SeriesepisodesResponse(
    val `data`: List<Episode>,
    val status: String
)

//data class SeriesDataItem(
//    val access_type: String,
//    val created_at: String,
//    val dynamic_link: String,
//    val media_file_url:String,
//    val is_active: Int,
//    val is_archived: Int,
//    val language_id: String,
//    val languages_info: String,
//    val series_id: String,
//    val series_ui_type: String,
//    val show_title: Boolean,
//    val status: String,
//    val sub_titles_info: String,
//    val thumb_image_bs2_url: String,
//    val title: String,
//    val total_shares: Int,
//    val total_views: Int,
//    val type: String,
//    val updated_at: String
//)