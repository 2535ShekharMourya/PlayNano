package com.zen.videoplayertestapp.data.remote

import com.brochill.minismodule.data.model.HomepageResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class ShortsSeriesRemoteDataSourceImp(private val apiService:ApiService?,private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO):ShortsSeriesRemoteDataSource {
    // Call Apis

//    override suspend fun fetchHomepageData(): Resource<HomepageResponse?> {
//        return try {
//            val response = minisAPIService?.getHomepageData()
//            Resource.Success(response?.body())
//        }
//        catch (noInternetException: UnknownHostException) {
//            val noInternetMessage = noInternetException.message?.substringBefore("\n")
//                ?: "Unable to resolve host: No address associated with hostname"
//            Resource.Failure(NetworkException.NoInternetConnection(noInternetMessage))
//        }
//        catch (serverError: NetworkException.ServerException) {
//            Resource.Failure(serverError)
//        }
//        catch (unAuthorisedException: NetworkException.UnAuthorisedException) {
//            Resource.Failure(unAuthorisedException)
//        }
//        catch (serializationException: NetworkException.SerializationException) {
//            Resource.Failure(serializationException)
//        }
//        catch (exception: Exception) {
//            exception.printStackTrace()
//            Resource.Failure(
//                NetworkException.UnKnownException(message = "api error unKnownException ${exception.message?.substringBefore("\n")}")
//            )
//        }
//
//    }


//    override suspend fun fetchCarouselData(): Resource<CarouselResponse?> = withContext(Dispatchers.IO)
//    {
//        try {
//
//            val response = minisAPIService?.getCarouselData()
//            Resource.Success(response?.body())
//        } catch (noInternetException: UnknownHostException) {
//            val noInternetMessage = noInternetException.message?.substringBefore("\n")
//                ?: "Unable to resolve host: No address associated with hostname"
//            Resource.Failure(NetworkException.NoInternetConnection(noInternetMessage))
//        }
//        catch (serverError: NetworkException.ServerException) {
//            Resource.Failure(serverError)
//        }
//        catch (unAuthorisedException: NetworkException.UnAuthorisedException) {
//            Resource.Failure(unAuthorisedException)
//        }
//        catch (serializationException: NetworkException.SerializationException) {
//            Resource.Failure(serializationException)
//        }
//        catch (exception: Exception) {
//            exception.printStackTrace()
//            Resource.Failure(
//                NetworkException.UnKnownException(message = "api error unKnownException ${exception.message?.substringBefore("\n")}")
//            )
//        }
//    }


}