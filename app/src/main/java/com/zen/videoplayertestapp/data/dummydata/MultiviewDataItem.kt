package com.zen.videoplayertestapp.data.dummydata

sealed class MultiviewDataItem {
    data class Carousel(val carousels: List<SeriesItem>): MultiviewDataItem()
    data class RecentlyViewed(val recentlyViewed: List<SeriesItem>): MultiviewDataItem()
    data class Banner(val bannerItem: Int): MultiviewDataItem()
    data class TopInIndia(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class TopInHindi(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class TopInTamil(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class BigBanner(val bigBannerItem: Int): MultiviewDataItem()
    data class TopInTelugu(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class TopInBengali(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class TopInKannada(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class TopInMalayalam(val topInIndia: List<SeriesItem>): MultiviewDataItem()
    data class FullScreenBanner(val fullScreenBanner: Int): MultiviewDataItem()
    data class YouMayLike(val youMayLike: List<SeriesItem>): MultiviewDataItem()
    data class ExploreMore(val exploreMore: List<SeriesItem>): MultiviewDataItem()
}