package com.zen.videoplayertestapp.data.repo

import android.content.Context
import android.util.Log
import com.brochill.minismodule.CarouselResponse
import com.brochill.minismodule.data.model.HomepageResponse
import com.brochill.minismodule.data.model.RecommendationsResponse
import com.brochill.minismodule.data.model.SeriesInfoResponse
import com.brochill.minismodule.data.model.SeriesepisodesResponse
import com.zen.videoplayertestapp.core.networkmodule.RetrofitAPIClient
import com.zen.videoplayertestapp.data.local.room.ShortsSeriesDao
import com.zen.videoplayertestapp.data.local.room.ShortsSeriesLocalDataSource
import com.zen.videoplayertestapp.data.remote.ApiService
import com.zen.videoplayertestapp.data.remote.ShortsSeriesRemoteDataSource

class RepositoryImp(  private val shortsSeriesRemoteDataSource: ShortsSeriesRemoteDataSource,
                    private val shortsSeriesLocalDataSource: ShortsSeriesLocalDataSource) : Repository{

    //    override suspend fun getHomepageData(): Resource<HomepageResponse?> = withContext(ioDispatcher) { minisRemoteDataSource.fetchHomepageData() }
//    override suspend fun getCarouselData(): Resource<CarouselResponse?> = withContext(ioDispatcher) { minisRemoteDataSource.fetchCarouselData() }


    // Read from assets folder
//    fun loadJSONFromAsset(context: Context, fileName: String): String {
//        return context.assets.open(fileName).bufferedReader().use { it.readText() }
//    }
//
//    // Usage
//    val mockResponse = loadJSONFromAsset(context, "mock_user_response.json")

    // convert this string response into kotlin objects and use : Gson,moshi, kotlinx.serialization any of them


    override suspend fun fetchHomepageData(): HomepageResponse? {
        return try {
            logMessage(" getHomepageData(): ${  RetrofitAPIClient.getInstance()?.getHomepageData()}")
            RetrofitAPIClient.getInstance()?.getHomepageData()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchCarouselData(): CarouselResponse? {
        return  try {
            logMessage("getCarouselData: ${RetrofitAPIClient.getInstance()?.getCarouselData()}")
            RetrofitAPIClient.getInstance()?.getCarouselData()
        } catch (e: Exception) {
            logMessage("failure ")
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchRecommendationsData(): RecommendationsResponse? {
        return try {
            logMessage("getRecommendationData: ${ RetrofitAPIClient.getInstance()?.getRecommendationData()} ")
            RetrofitAPIClient.getInstance()?.getRecommendationData()
        } catch (e: Exception) {
            logMessage("failure ")
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchSeriesEpisodesData(): SeriesepisodesResponse? {
        return try {
            logMessage("  fetchSeriesEpisodesData : ${ RetrofitAPIClient.getInstance()?.getSeriesEpisodesData()} ")
            RetrofitAPIClient.getInstance()?.getSeriesEpisodesData()
        } catch (e: Exception) {
            logMessage("failure ")
            e.printStackTrace()
            null
        }
    }

    override suspend fun fetchSeriesInfoData(): SeriesInfoResponse? {
        return try {
            logMessage(" getSeriesInfoData: ${RetrofitAPIClient.getInstance()?.getSeriesInfoData()}")
            RetrofitAPIClient.getInstance()?.getSeriesInfoData()
        } catch (e: Exception) {
            logMessage("failure ")
            e.printStackTrace()
            null
        }
    }
    fun logMessage(message: String) {
        Log.d("MinisRepositoryImp", message)
    }
}