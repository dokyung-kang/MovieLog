package ddwu.com.mobile.movieapp

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

    val TAG = "MainActivity"

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

        /*RecyclerView 의 layoutManager 지정*/
        binding.diaryRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        /*샘플 데이터, DB 사용 시 DB에서 읽어온 데이터로 대체 필요*/
        val diarys = ArrayList<Diary>()
        diarys.add(Diary(1,"된장찌개", "한국"))
        diarys.add(Diary(2,"김치찌개", "한국"))
        diarys.add(Diary(3,"마라탕", "중국"))
        diarys.add(Diary(4,"훠궈", "중국"))
        diarys.add(Diary(5,"스시", "일본"))
        diarys.add(Diary(6,"오코노미야키", "일본"))

        diaryAdapter = DetailAdapter(diarys)

        /*foodAdapter 에 LongClickListener 구현 및 설정*/
        val onLongClickListener = object: DetailAdapter.OnItemLongClickListener {
            override fun onItemLongClickListener(view: View, pos: Int) {
                Log.d(TAG, "Long Click!! $pos")
            }
        }
        diaryAdapter.setOnItemLongClickListener(onLongClickListener)

        binding.diaryRecyclerView.adapter = diaryAdapter

        showAllDiarys()

        binding.btnShow.setOnClickListener{
            showDiaryByTitle(binding.etTItle.getText().toString())
        }

        binding.btnInsert.setOnClickListener{
            addDiary( Diary(0, binding.etTItle.getText().toString(), binding.etContent.getText().toString()) )
        }

        binding.btnUpdate.setOnClickListener {
            modifyDiary( binding.etTItle.getText().toString(), binding.etContent.getText().toString() )
        }

        binding.btnDelete.setOnClickListener {
            removeDiary(  binding.etTItle.getText().toString() )

        }
    }

    fun addDiary(diary: Diary) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.insertDiary(diary)
        }
    }

    fun modifyDiary(title: String, dContent: String) {
        CoroutineScope(Dispatchers.IO).launch {
            diaryDao.updateDiary(title, dContent)
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

    fun showAllDiarys() {


        CoroutineScope(Dispatchers.IO).launch {
//        초기 값 미리 db에 세팅 (showAllFoods() 제대로 작동되는지 확인용
//        addFood(Food(1,"된장찌개", "한국"))
//        addFood(Food(2,"김치찌개", "한국"))
//        addFood(Food(3,"마라탕", "중국"))
//        addFood(Food(4,"훠궈", "중국"))
//        addFood(Food(5,"스시", "일본"))
//        addFood(Food(6,"오코노미야키", "일본"))
            val flowDiarys: Flow<List<Diary>> = diaryDao.getAllDiarys()
            flowDiarys.collect{ diarys ->
                for (diary in diarys) {
                    Log.d(TAG, diary.toString())
                }
            }
        }
    }
}