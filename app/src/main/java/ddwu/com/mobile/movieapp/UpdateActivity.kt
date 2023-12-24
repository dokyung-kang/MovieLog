package ddwu.com.mobile.movieapp

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

        binding.btnUpdate.setOnClickListener {
            modifyDiary( Diary(diaryID, binding.etTItleU.getText().toString(), binding.etMovieNmU.getText().toString(),
                binding.etCinemaU.getText().toString(), binding.etCrateDateU.getText().toString(),
                binding.etTimeU.getText().toString(), binding.etContentU.getText().toString()) )
        }

        binding.btnDelete.setOnClickListener {
            removeDiary(  diaryID )

        }
    }

    fun modifyDiary(diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.updateDiary(diary._id, diary.title.toString(),
                diary.movieNm.toString(), diary.cimena.toString(),
                diary.createDate.toString(), diary.createTime.toString(), diary.dContent.toString())
        }
    }

    fun removeDiary(diaryID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.deleteDiay(diaryID)
        }
    }

}