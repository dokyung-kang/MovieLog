package ddwu.com.mobile.movielog.network

import ddwu.com.mobile.movielog.data.PlaceRoot
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
        @Query("display") display: Int,
    )  : Call<PlaceRoot>
}