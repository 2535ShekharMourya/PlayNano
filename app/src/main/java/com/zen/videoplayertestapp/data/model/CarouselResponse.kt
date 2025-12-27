package com.brochill.minismodule

import com.google.gson.annotations.SerializedName


data class CarouselResponse(

    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var data: ArrayList<CarouselModel> = arrayListOf()

)

data class CarouselModel(

    @SerializedName("title") var title: String? = null,
    @SerializedName("media_file_url") var mediaFileUrl: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("carousel_id") var carouselId: String? = null,
    @SerializedName("series_ui_type") var seriesUiType: String? = null,
    @SerializedName("thumb_image_bs2_url") var thumbImageBs2Url: String? = null,
    @SerializedName("language_id") var languageId: String? = null,
    @SerializedName("is_active") var isActive: Int? = null,
    @SerializedName("access_type") var accessType: String? = null,
    @SerializedName("is_archived") var isArchived: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("languages_info") var languagesInfo: String? = null,
    @SerializedName("sub_titles_info") var subTitlesInfo: String? = null,
    @SerializedName("total_views") var totalViews: Int? = null,
    @SerializedName("total_shares") var totalShares: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("dynamic_link") var dynamicLink: String? = null

)