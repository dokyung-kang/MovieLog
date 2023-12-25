package ddwu.com.mobile.movieapp.network

import ddwu.com.mobile.movieapp.data.PlaceRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IPlaceAPIService {
    @GET("v1/search/local.json")
    fun getPlacesByKeyword (
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") keyword: String,
    )  : Call<PlaceRoot>
}