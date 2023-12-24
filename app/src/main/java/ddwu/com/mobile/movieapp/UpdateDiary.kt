package ddwu.com.mobile.movieapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.movieapp.data.Diary
import ddwu.com.mobile.movieapp.data.DiaryDao
import ddwu.com.mobile.movieapp.data.DiaryDatabase
import ddwu.com.mobile.movieapp.databinding.ActivityDetailBinding
import ddwu.com.mobile.movieapp.databinding.ActivityUpdateBinding
import ddwu.com.mobile.movieapp.ui.DetailAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDiary : AppCompatActivity()  {

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


        /*샘플 데이터, DB 사용 시 DB에서 읽어온 데이터로 대체 필요*/
        val diarys = ArrayList<Diary>()

        diaryAdapter = DetailAdapter(diarys)

        /*foodAdapter 에 LongClickListener 구현 및 설정*/
        val onLongClickListener = object: DetailAdapter.OnItemLongClickListener {
            override fun onItemLongClickListener(view: View, pos: Int) {
                Log.d(TAG, "Long Click!! $pos")
            }
        }
        diaryAdapter.setOnItemLongClickListener(onLongClickListener)

        binding.btnShow.setOnClickListener{
            showDiaryByTitle(binding.etTItleU.getText().toString())
        }

        binding.btnUpdate.setOnClickListener {
            modifyDiary( Diary(0, binding.etTItleU.getText().toString(), binding.etMovieNmU.getText().toString(),
                binding.etCinemaU.getText().toString(), binding.etCrateDateU.getText().toString(),
                binding.etTimeU.getText().toString(), binding.etContentU.getText().toString()) )
        }

        binding.btnDelete.setOnClickListener {
            removeDiary(  binding.etTItleU.getText().toString() )

        }
    }

    fun modifyDiary(diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.updateDiary(diary)
        }
    }

    fun removeDiary(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.deleteDiay(title)
        }
    }

    fun showDiaryByTitle(country: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val diarys = diaryDao.getDiaryByTitle(country)
            for (diary in diarys) {
                Log.d(TAG, diary.toString())
            }
        }
    }
}