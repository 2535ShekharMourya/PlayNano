package com.zen.videoplayertestapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brochill.minismodule.CarouselModel
import com.brochill.minismodule.CarouselResponse
import com.brochill.minismodule.data.model.Episode
import com.brochill.minismodule.data.model.HomepageResponse
import com.brochill.minismodule.data.model.RecommendationsResponse
import com.brochill.minismodule.data.model.Series
import com.brochill.minismodule.data.model.SeriesInfoResponse
import com.brochill.minismodule.data.model.SeriesepisodesResponse
import com.zen.videoplayertestapp.core.networkmodule.RetrofitAPIClient
import com.zen.videoplayertestapp.data.repo.Repository
import kotlinx.coroutines.launch

class CentralizeViewmodel(private val repo: Repository) : ViewModel() {
    private val _homepageData = MutableLiveData<HomepageResponse?>()
    val homepageData: MutableLiveData<HomepageResponse?> get() = _homepageData
    private val _carouselpageData = MutableLiveData<CarouselResponse?>()
    val carouselData: MutableLiveData<CarouselResponse?> get() = _carouselpageData
    private val _recommendatoinsData = MutableLiveData<RecommendationsResponse?>()
    val recommendationData: MutableLiveData<RecommendationsResponse?> get() = _recommendatoinsData
    private val _seriesEpisodesData = MutableLiveData<SeriesepisodesResponse?>()
    val seriesEpisodesData: MutableLiveData<SeriesepisodesResponse?> get() = _seriesEpisodesData
    private val _seriesEpisodesData2 = MutableLiveData<SeriesepisodesResponse?>()
    val seriesEpisodesData2: MutableLiveData<SeriesepisodesResponse?> get() = _seriesEpisodesData2
    private val _seriesInfoData = MutableLiveData<SeriesInfoResponse?>()
    val seriesInfoData: MutableLiveData<SeriesInfoResponse?> get() = _seriesInfoData
    var itemData: Episode?=null
    var itemData2: Series?=null
    var carouseDataItem: CarouselModel?=null
    private val _data = MutableLiveData<Episode?>()
    val data: MutableLiveData<Episode?> get() = _data
    val episodeItem = MutableLiveData<CarouselModel>()
    val episodeDataList = MutableLiveData<MutableList<Episode>>()
    val episodeDataList2 = MutableLiveData<MutableList<Episode>>()
    val seriesDataList = MutableLiveData<MutableList<Series>>()
    val seriesCollectionList = MutableLiveData<MutableList<Series>>()
    val seriesCollectionTitle = MutableLiveData<String>()
    var seriesItem = MutableLiveData<Series>()
    var keymatching:String? =null
    var position:Int?=null




    fun setPosition(i: Int){
        position = i
    }
    fun setKey(key:String?){
        keymatching = key
    }
    fun setData(homepageResponse: Episode?) {
        itemData = homepageResponse
    }
    fun setCarouselData(carouselItem: CarouselModel?) {
        carouseDataItem = carouselItem
    }
    fun setData2(homepageResponse: Series?) {
        itemData2 = homepageResponse
    }

    // Call this function to fetch data from API
    fun fetchHomepageData() {
        viewModelScope.launch {
            _homepageData.postValue(repo.fetchHomepageData())
        }
    }
    // Call this function to fetch data from API
    fun fetchCarouselData() {
        viewModelScope.launch {
            _carouselpageData.postValue(repo.fetchCarouselData())
        }
    }

    fun fetchRecommendationsData() {
        viewModelScope.launch {
            _recommendatoinsData.postValue(repo.fetchRecommendationsData())
        }
    }

    fun fetchSeriesEpisodesData() {
        viewModelScope.launch {
            _seriesEpisodesData.postValue(repo.fetchSeriesEpisodesData())
        }
    }
    fun fetchSeriesEpisodesData2() {
        viewModelScope.launch {
            try {
                val response = RetrofitAPIClient.getInstance()?.getSeriesEpisodesData2()
                _seriesEpisodesData2.postValue(response) // Update LiveData with the API response
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle errors (e.g., log or show error to UI)
            }
        }
    }

    fun fetchSeriesInfoData(){
        viewModelScope.launch {
            _seriesInfoData.postValue(repo.fetchSeriesInfoData())
        }
    }

}