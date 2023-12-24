//package com.example.movieapp
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.example.movieapp.data.MovieRoot
//import com.example.movieapp.databinding.ActivityMainBinding
//import com.example.movieapp.network.IMovieAPIService
//import com.example.movieapp.ui.MovieAdapter
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class SearchActivity : AppCompatActivity() {
//    private val TAG = "MainActivity"
//
//    lateinit var mainBinding : ActivityMainBinding
//    lateinit var adapter : MovieAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mainBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(mainBinding.root)
//
//
//        adapter = MovieAdapter()
//        mainBinding.rvMovies.adapter = adapter
//        mainBinding.rvMovies.layoutManager = LinearLayoutManager(this)
//
//
//        adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListner {
//            override fun onItemClick(view: View, position: Int) {
//                // RecyclerView 항목 클릭 시 해당 위치의 Item 이 갖고 있는 image 를 Glide 에 전달
////                val url = adapter.movies!![position]?.image.toString()
//                val url = adapter.movies?.get(position)?.image
//                Glide.with(view)
//                    .load(url)
//                    .into(mainBinding.imageView)
//            }
//        })
//
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(resources.getString(R.string.naver_api_url))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val service = retrofit.create(IMovieAPIService::class.java)
//
//
//        mainBinding.btnSearch.setOnClickListener {
//            val keyword = mainBinding.etKeyword.text.toString()
//
//            val apiCall = service.getMoviesByTitle(
//                resources.getString(R.string.client_id),
//                resources.getString(R.string.client_secret),
//                keyword
//            )
//
//            apiCall.enqueue(
//                object: Callback<MovieRoot> {
//                    override fun onResponse(call: Call<MovieRoot>, response: Response<MovieRoot>) {
//                        val movieRoot = response.body()
//                        adapter.movies = movieRoot?.items
//                        adapter.notifyDataSetChanged()
//                    }
//
//                    override fun onFailure(call: Call<MovieRoot>, t: Throwable) {
//                    }
//
//                }
//            )
//        }
//    }
//}