package ddwu.com.mobile.movieapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.movieapp.data.Diary
import ddwu.com.mobile.movieapp.data.DiaryDao
import ddwu.com.mobile.movieapp.data.DiaryDatabase
import ddwu.com.mobile.movieapp.databinding.ActivityUpdateBinding
import ddwu.com.mobile.movieapp.ui.DetailAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity()  {

    val TAG = "UpdateActivity"

    lateinit var binding: ActivityUpdateBinding
    lateinit var diaryAdapter: DetailAdapter

    lateinit var db : DiaryDatabase
    lateinit var diaryDao : DiaryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DiaryDatabase.getDatabase(this)
        diaryDao = db.diaryDao()

        val diaryID = intent.getIntExtra("diaryID", 0)
        binding.etTItleU.setText(intent.getStringExtra("diaryTitle"))
        binding.etMovieNmU.setText(intent.getStringExtra("diaryMovieNm"))
        binding.etCinemaU.setText(intent.getStringExtra("diaryCinema"))
        binding.etCrateDateU.setText(intent.getStringExtra("diaryCreateDate"))
        binding.etTimeU.setText(intent.getStringExtra("diaryCreateTime"))
        binding.etContentU.setText(intent.getStringExtra("diaryContent"))

        binding.findMNmU.setOnClickListener {
            var intent2 = Intent(this, SearchActivity::class.java)
            intent2.putExtra("whatType",  "oldWrite")
            intent2.putExtra("diaryID",  diaryID)
            intent2.putExtra("diaryTitle",  binding.etTItleU.text.toString())
            intent2.putExtra("diaryCinema",  binding.etCinemaU.text.toString())
            intent2.putExtra("diaryCreateDate",  binding.etCrateDateU.text.toString())
            intent2.putExtra("diaryCreateTime",  binding.etTimeU.text.toString())
            intent2.putExtra("diaryContent",  binding.etContentU.text.toString())
            startActivity(intent2)
        }
        binding.findCiU.setOnClickListener {
            var intent2 = Intent(this, MapActivity::class.java)
            intent2.putExtra("whatType",  "oldWrite")
            intent2.putExtra("diaryID",  diaryID)
            intent2.putExtra("diaryTitle",  binding.etTItleU.text.toString())
            intent2.putExtra("diaryMovieNm",  binding.etMovieNmU.text.toString())
            intent2.putExtra("diaryCreateDate",  binding.etCrateDateU.text.toString())
            intent2.putExtra("diaryCreateTime",  binding.etTimeU.text.toString())
            intent2.putExtra("diaryContent",  binding.etContentU.text.toString())
            startActivity(intent2)
        }

        binding.btnUpdate.setOnClickListener {
            modifyDiary( Diary(diaryID, binding.etTItleU.text.toString(), binding.etMovieNmU.text.toString(),
                binding.etCinemaU.text.toString(), binding.etCrateDateU.text.toString(),
                binding.etTimeU.text.toString(), binding.etContentU.text.toString()) )
        }

        binding.btnDelete.setOnClickListener {
            removeDiary(  diaryID )
        }

        binding.btnCancelU.setOnClickListener {
            var intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }
    }

    fun modifyDiary(diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.updateDiary(diary._id, diary.title.toString(),
                diary.movieNm.toString(), diary.cimena.toString(),
                diary.createDate.toString(), diary.createTime.toString(), diary.dContent.toString())
        }
        var intent = Intent(this, DiaryActivity::class.java)
        startActivity(intent)
    }

    fun removeDiary(diaryID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.deleteDiay(diaryID)
        }
        var intent = Intent(this, DiaryActivity::class.java)
        startActivity(intent)
    }

}