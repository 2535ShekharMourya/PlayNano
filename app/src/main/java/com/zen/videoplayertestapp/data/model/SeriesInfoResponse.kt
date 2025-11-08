package com.brochill.minismodule.data.model

data class SeriesInfoResponse(
    val `data`: List<SeriesItemInfo>,
    val status: String
)

data class SeriesItemInfo(
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
    val title: String,
    val type: String,
    val updated_at: String
)