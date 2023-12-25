package ddwu.com.mobile.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.movieapp.data.Diary
import ddwu.com.mobile.movieapp.data.DiaryDao
import ddwu.com.mobile.movieapp.data.DiaryDatabase
import ddwu.com.mobile.movieapp.databinding.ActivityDetailBinding
import ddwu.com.mobile.movieapp.ui.DetailAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    val TAG = "DetailActivity"

    lateinit var binding: ActivityDetailBinding

    lateinit var db : DiaryDatabase
    lateinit var diaryDao : DiaryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DiaryDatabase.getDatabase(this)
        diaryDao = db.diaryDao()

        binding.etTItle.setText(intent.getStringExtra("writeTitle"))
        binding.etCinema.setText(intent.getStringExtra("writePlace"))
        binding.etMovieNm.setText(intent.getStringExtra("writeMovie"))
        binding.etCrateDate.setText(intent.getStringExtra("writeDate"))
        binding.etTime.setText(intent.getStringExtra("writeTime"))
        binding.etContent.setText(intent.getStringExtra("writeContent"))

        binding.findMNm.setOnClickListener {
            var intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("whatType",  "newWrite")
            intent.putExtra("writeTitle",  binding.etTItle.text.toString())
            intent.putExtra("writePlace",  binding.etCinema.text.toString())
            intent.putExtra("writeDate",  binding.etCrateDate.text.toString())
            intent.putExtra("writeTime",  binding.etTime.text.toString())
            intent.putExtra("writeContent",  binding.etContent.text.toString())
            startActivity(intent)
        }
        binding.findCi.setOnClickListener {
            var intent = Intent(this, MapActivity::class.java)
            intent.putExtra("whatType",  "newWrite")
            intent.putExtra("writeTitle",  binding.etTItle.text.toString())
            intent.putExtra("writeMovie",  binding.etMovieNm.text.toString())
            intent.putExtra("writeDate",  binding.etCrateDate.text.toString())
            intent.putExtra("writeTime",  binding.etTime.text.toString())
            intent.putExtra("writeContent",  binding.etContent.text.toString())
            startActivity(intent)
        }

        binding.btnInsert.setOnClickListener{
            addDiary( Diary(0, binding.etTItle.getText().toString(), binding.etMovieNm.getText().toString(),
                binding.etCinema.getText().toString(), binding.etCrateDate.getText().toString(),
                binding.etTime.getText().toString(), binding.etContent.getText().toString()) )
        }

    }

    fun addDiary(diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.insertDiary(diary)
        }
        var intent = Intent(this, DiaryActivity::class.java)
        startActivity(intent)
    }
}