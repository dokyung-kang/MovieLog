package ddwu.com.mobile.movieapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ddwu.com.mobile.movieapp.data.Root
import ddwu.com.mobile.movieapp.databinding.ActivityMovieBinding
import ddwu.com.mobile.movieapp.network.IMovieOfficeAPIService
import ddwu.com.mobile.movieapp.ui.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity()  {
    private val TAG = "Searchctivity"

    lateinit var mainBinding : ActivityMovieBinding
    lateinit var adapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        adapter = MovieAdapter()
        mainBinding.rvMovies.adapter = adapter
        mainBinding.rvMovies.layoutManager = LinearLayoutManager(this)


        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val service = retrofit.create(IMovieOfficeAPIService::class.java)


        mainBinding.btnSearch.setOnClickListener {
            val movieNm = mainBinding.etDate.text.toString()

            val apiCallback = object: Callback<Root> {

                override fun onResponse(call: Call<Root>, response: Response<Root>) {
                    if (response.isSuccessful) {
                        val root : Root? = response.body()
                        adapter.movies = root?.movieListResult?.movieList
                        adapter.notifyDataSetChanged()

                    } else {
                        Log.d(TAG, "Unsuccessful Response")
                    }
                }

                override fun onFailure(call: Call<Root>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }

            }

            val apiCall : Call<Root>
                    = service.getDailyBoxOffice(
                "json",
                resources.getString(R.string.kobis_key),
                movieNm )

            apiCall.enqueue(apiCallback)

        }


        val url = resources.getString(R.string.image_url)
        Glide.with(this)
            .load(url)
            .into(mainBinding.imageView)


    }
}