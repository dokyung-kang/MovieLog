package ddwu.com.mobile.movielog.network

import ddwu.com.mobile.movielog.data.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieOfficeAPIService {
    @GET("kobisopenapi/webservice/rest/movie/searchMovieList.{type}")
    fun getDailyBoxOffice(
        @Path("type") type: String,
        @Query("key") key: String,
        @Query ("movieNm") targetDate: String,
    ) : Call<Root>
}

