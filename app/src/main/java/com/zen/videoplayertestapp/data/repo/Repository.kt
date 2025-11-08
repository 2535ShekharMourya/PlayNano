package com.zen.videoplayertestapp.data.repo

import com.brochill.minismodule.CarouselResponse
import com.brochill.minismodule.data.model.HomepageResponse
import com.brochill.minismodule.data.model.RecommendationsResponse
import com.brochill.minismodule.data.model.SeriesInfoResponse
import com.brochill.minismodule.data.model.SeriesepisodesResponse

interface Repository {
    suspend fun fetchHomepageData(): HomepageResponse?
    suspend fun fetchCarouselData(): CarouselResponse?
    suspend fun fetchRecommendationsData(): RecommendationsResponse?
    suspend fun fetchSeriesEpisodesData(): SeriesepisodesResponse?
    suspend fun fetchSeriesInfoData(): SeriesInfoResponse?
}