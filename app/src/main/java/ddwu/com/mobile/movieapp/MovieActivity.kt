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

        binding.mvBtn.setOnClickListener {
            if (intent.getStringExtra("whatType").toString().equals("newWrite")){
                var intent2 = Intent(this, DetailActivity::class.java)
                intent2.putExtra("writeMovie",  binding.tvMov2.text.toString())
                intent2.putExtra("writeTitle",  intent.getStringExtra("writeTitle"))
                intent2.putExtra("writePlace",  intent.getStringExtra("writePlace"))
                intent2.putExtra("writeDate",  intent.getStringExtra("writeDate"))
                intent2.putExtra("writeTime",  intent.getStringExtra("writeTime"))
                intent2.putExtra("writeContent",  intent.getStringExtra("writeContent"))
                startActivity(intent2)
            }
            else if (intent.getStringExtra("whatType").toString().equals("oldWrite")){
                var intent2 = Intent(this, UpdateActivity::class.java)
                intent2.putExtra("diaryMovieNm",  binding.tvMov2.text.toString())
                intent2.putExtra("diaryID",  intent.getIntExtra("diaryID", 0))
                intent2.putExtra("diaryTitle",  intent.getStringExtra("diaryTitle"))
                intent2.putExtra("diaryCinema",  intent.getStringExtra("diaryCinema"))
                intent2.putExtra("diaryCreateDate",  intent.getStringExtra("diaryCreateDate"))
                intent2.putExtra("diaryCreateTime",  intent.getStringExtra("diaryCreateTime"))
                intent2.putExtra("diaryContent",  intent.getStringExtra("diaryContent"))
                startActivity(intent2)
            }
            else{
                var intent2 = Intent(this, DetailActivity::class.java)
                intent2.putExtra("writeMovie",  binding.tvMov2.text.toString())
                startActivity(intent2)
            }
        }

        binding.mvBtn2.setOnClickListener {
            var intent2 = Intent(this, SearchActivity::class.java)
            startActivity(intent2)
        }
    }
}