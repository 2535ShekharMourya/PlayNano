package com.brochill.minismodule.data.model

import kotlinx.serialization.Serializable

data class HorizontalItemResponse(
    val title: String,
    val episodes: List<Episode> ? = null,
    val access_type: String,
    val series: List<Series>? = null
   // val created_at: String,
   // val dynamic_link: String,
    //val is_active: Int,
   // val is_archived: Int,
   // val language_id: String,
    //val series: List<Series>,
  //  val series_id: String,
    //val series_ui_type: String,
   // val show_title: Boolean,
   // val status: String,
   // val thumb_image_bs2_url: String,
  //  val type: String,
  //  val updated_at: String

)

//data class EpisodeItem(
//    val access_type: String,
//    val created_at: String,
//    val dynamic_link: String,
//    var mediaFileUrl: String? = null,
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
