package com.zen.videoplayertestapp.data.remote

import com.brochill.minismodule.CarouselResponse
import com.brochill.minismodule.data.model.HomepageResponse
import com.brochill.minismodule.data.model.RecommendationsResponse
import com.brochill.minismodule.data.model.SeriesInfoResponse
import com.brochill.minismodule.data.model.SeriesepisodesResponse
import retrofit2.http.GET

interface ApiService {
    @GET("carousel")
    suspend fun getCarouselData(): CarouselResponse

    @GET("homepage")
    suspend fun getHomepageData(): HomepageResponse

    @GET("seriesInfo")
    suspend fun getSeriesInfoData(): SeriesInfoResponse

    @GET("series-episodes")
    suspend fun getSeriesEpisodesData(): SeriesepisodesResponse

    @GET("series-episodes-2")
    suspend fun getSeriesEpisodesData2(): SeriesepisodesResponse

    @GET("recommendations")
    suspend fun getRecommendationData(): RecommendationsResponse

}