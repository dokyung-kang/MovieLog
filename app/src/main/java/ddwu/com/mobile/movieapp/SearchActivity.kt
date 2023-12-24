package ddwu.com.mobile.movieapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

    lateinit var binding : ActivityMovieBinding
    lateinit var adapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter()
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(binding.rvMovies.getContext(), LinearLayoutManager(this).orientation)
        binding.rvMovies.addItemDecoration(dividerItemDecoration)

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.kobis_url))
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val service = retrofit.create(IMovieOfficeAPIService::class.java)


        binding.btnSearch.setOnClickListener {
            val movieNm = binding.etDate.text.toString()

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

        val onClickListener = object: MovieAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, pos: Int) {
                Log.d(TAG, "Short Click!! $pos")

                val intent = Intent(this@SearchActivity, MovieActivity::class.java)
                intent.putExtra("movieCd", adapter.movies!![pos]?.movieCd.toString())
                intent.putExtra("movieNm", adapter.movies!![pos]?.movieNm.toString())
                intent.putExtra("movieNmEn", adapter.movies!![pos]?.movieNmEn.toString())
                intent.putExtra("prdtYear", adapter.movies!![pos]?.prdtYear.toString())
                intent.putExtra("openDt", adapter.movies!![pos]?.openDt.toString())
                intent.putExtra("typeNm", adapter.movies!![pos]?.typeNm.toString())
                intent.putExtra("genreAlt", adapter.movies!![pos]?.genreAlt.toString())
                intent.putExtra("directors", adapter.movies!![pos]?.directors?.getOrNull(0)?.peopleNm ?: "".toString())

                startActivity(intent)
            }
        }

        adapter.setOnItemClickListener(onClickListener)

        binding.rvMovies.adapter = adapter

    }
}