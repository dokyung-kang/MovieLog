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
    lateinit var diaryAdapter: DetailAdapter

    lateinit var db : DiaryDatabase
    lateinit var diaryDao : DiaryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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