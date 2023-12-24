package ddwu.com.mobile.movieapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.movieapp.data.Diary
import ddwu.com.mobile.movieapp.data.DiaryDao
import ddwu.com.mobile.movieapp.data.DiaryDatabase
import ddwu.com.mobile.movieapp.databinding.ActivityMovieinfoBinding
import ddwu.com.mobile.movieapp.ui.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieActivity : AppCompatActivity()  {
    val TAG = "MovieActivity"


    lateinit var binding: ActivityMovieinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvMov1.setText(intent.getStringExtra("movieCd"))
        binding.tvMov2.setText(intent.getStringExtra("movieNm"))
        binding.tvMov3.setText(intent.getStringExtra("movieNmEn"))
        binding.tvMov4.setText(intent.getStringExtra("prdtYear"))
        binding.tvMov5.setText(intent.getStringExtra("openDt"))
        binding.tvMov6.setText(intent.getStringExtra("typeNm"))
        binding.tvMov7.setText(intent.getStringExtra("genreAlt"))
        binding.tvMov8.setText(intent.getStringExtra("directors"))
    }
}