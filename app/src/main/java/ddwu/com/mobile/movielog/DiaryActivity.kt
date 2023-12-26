package ddwu.com.mobile.movielog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.movielog.data.Diary
import ddwu.com.mobile.movielog.data.DiaryDao
import ddwu.com.mobile.movielog.data.DiaryDatabase
import ddwu.com.mobile.movielog.databinding.ActivityDiaryBinding
import ddwu.com.mobile.movielog.ui.DiaryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DiaryActivity : AppCompatActivity()  {
    val TAG = "DiaryActivity"

    lateinit var binding: ActivityDiaryBinding
    lateinit var diaryAdapter: DiaryAdapter

    lateinit var db : DiaryDatabase
    lateinit var diaryDao : DiaryDao
    val diarys = ArrayList<Diary>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DiaryDatabase.getDatabase(this)
        diaryDao = db.diaryDao()

        /*RecyclerView 의 layoutManager 지정*/
        binding.rvDiarys.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        val dividerItemDecoration = DividerItemDecoration(binding.rvDiarys.getContext(), LinearLayoutManager(this).orientation)
        binding.rvDiarys.addItemDecoration(dividerItemDecoration)

        binding.crateBtn.setOnClickListener {
            var intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        showAllDiarys()

        diaryAdapter = DiaryAdapter(diarys)

        val onLongClickListener = object: DiaryAdapter.OnItemLongClickListener {
            override fun onItemLongClickListener(view: View, pos: Int) {
                Log.d(TAG, "Long Click!! $pos")
            }
        }

        val onClickListener = object: DiaryAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, pos: Int) {
                Log.d(TAG, "Short Click!! $pos")

                val intent = Intent(this@DiaryActivity, UpdateActivity::class.java)
                intent.putExtra("diaryID", diarys[pos]._id)
                intent.putExtra("diaryTitle", diarys[pos].title)
                intent.putExtra("diaryMovieNm", diarys[pos].movieNm)
                intent.putExtra("diaryCinema", diarys[pos].cimena)
                intent.putExtra("diaryCreateDate", diarys[pos].createDate)
                intent.putExtra("diaryCreateTime", diarys[pos].createTime)
                intent.putExtra("diaryContent", diarys[pos].dContent)

                startActivity(intent)
            }
        }

        diaryAdapter.setOnItemLongClickListener(onLongClickListener)
        diaryAdapter.setOnItemClickListener(onClickListener)

        binding.rvDiarys.adapter = diaryAdapter

        showAllDiarys()

    }

    fun showAllDiarys() {
        CoroutineScope(Dispatchers.IO).launch {
            val flowDiarys: Flow<List<Diary>> = diaryDao.getAllDiarys()
            flowDiarys.collect{ diaries ->
                diarys.clear()
                for (diary in diaries) {
                    diarys.add(Diary(diary._id, diary.title, diary.movieNm, diary.cimena, diary.createDate, diary.createTime, diary.dContent))
                }
                runOnUiThread {
                    diaryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}

