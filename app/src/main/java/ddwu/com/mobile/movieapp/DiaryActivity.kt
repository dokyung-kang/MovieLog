//package ddwu.com.mobile.movieapp
//
//import android.content.DialogInterface
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import ddwu.com.mobile.movieapp.data.Diary
//import ddwu.com.mobile.movieapp.databinding.ActivityDiaryBinding
//import ddwu.com.mobile.movieapp.ui.DiaryAdapter
//
//class DiaryActivity : AppCompatActivity()  {
//    val TAG = "DiaryActivity"
//    val REQ_ADD = 100
//    val REQ_UPDATE = 200
//
//
//    lateinit var binding : ActivityDiaryBinding
//    lateinit var adapter : DiaryAdapter
//    lateinit var diarys : ArrayList<Diary>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDiaryBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        /*RecyclerView 의 layoutManager 지정*/
//        binding.rvDiarys.layoutManager = LinearLayoutManager(this).apply {
//            orientation = LinearLayoutManager.VERTICAL
//        }
//
//        diarys = getAllDiarys()               // DB 에서 모든 diary를 가져옴
//        adapter = DiaryAdapter(diarys)        // adapter 에 데이터 설정
//        binding.rvDiarys.adapter = adapter   // RecylcerView 에 adapter 설정
//
//
//        /*RecyclerView 항목 클릭 시 실행할 객체*/
//        val onClickListener = object : DiaryAdapter.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {
//                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
//                val intent = Intent(this@DiaryActivity, DetailActivity::class.java)
//                intent.putExtra("dto", diarys.get(position) )
//                startActivityForResult(intent, REQ_UPDATE)
//            }
//        }
//        adapter.setOnItemClickListener(onClickListener)
//
//
//        /*RecyclerView 항목 롱클릭 시 실행할 객체*/
//        val onLongClickListener = object: DiaryAdapter.OnItemLongClickListener {
//            override fun onItemLongClick(view: View, position: Int) {
//                /*롱클릭 항목의 dto 에서 id 확인 후 함수에 전달*/
//                AlertDialog.Builder(this@DiaryActivity).run {
//                    setTitle("다이어리 삭제")
//                    setMessage("\' ${diarys.get(position).title} \' 을/를 삭제하시겠습니까?")
//                    setPositiveButton("확인", object : DialogInterface.OnClickListener{
//                        /*삭제 확인 클릭 시 삭제 후 갱신*/
//                        override fun onClick(p0: DialogInterface?, p1: Int) {
//                            if ( deleteDiary(diarys.get(position)._id) > 0) {
//                                refreshList(RESULT_OK)
//                                Toast.makeText(this@DiaryActivity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    })
//                    setNegativeButton("취소", null)
//                    show()
//                }
//            }
//        }
//        adapter.setOnItemLongClickListener(onLongClickListener)
//    }
//
//    /*화면이 보일 때마다 화면을 갱신하고자 할 경우에는 onResume()에 갱신작업 추가*/
////    override fun onResume() {
////        super.onResume()
////        adapter.notifyDataSetChanged()
////    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQ_UPDATE -> {
//                refreshList(resultCode)
//            }
//            REQ_ADD -> {
//                refreshList(resultCode)
//            }
//        }
//    }
//
//    /*다른 액티비티에서 DB 변경 시 DB 내용을 읽어 Adapter의 list 에 반영하고 RecyclerView를 갱신*/
//    private fun refreshList(resultCode: Int) {
//        if (resultCode == RESULT_OK) {
//            diarys.clear()
//            diarys.addAll(getAllDiarys())
//            adapter.notifyDataSetChanged()
//        } else {
//            Toast.makeText(this@DiaryActivity, "취소되었습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    /*전체 레코드를 가져와 dto 로 저장한 후 dto를 저장한 list 반환*/
//    fun getAllDiarys() : ArrayList<Diary> {
//        val helper = DiaryDBHelper(this)
//        val db = helper.readableDatabase
//        val cursor = db.query(DiaryDBHelper.TABLE_NAME, null, null, null, null, null, null)
//
//        val diarys = arrayListOf<Diary>()
//
//        with (cursor) {
//            while (moveToNext()) {
//                val id = getInt( getColumnIndex(BaseColumns._ID) )
//                val title = getString ( getColumnIndex(DiaryDBHelper.COL_TITLE) )
//                val date = getString ( getColumnIndex(DiaryDBHelper.COL_DATE) )
//                val weather = getString ( getColumnIndex(DiaryDBHelper.COL_WEATHER) )
//                val place = getString ( getColumnIndex(DiaryDBHelper.COL_PLACE) )
//                val music = getString ( getColumnIndex(DiaryDBHelper.COL_MUSIC) )
//                val story = getString ( getColumnIndex(DiaryDBHelper.COL_STORY) )
//                val dto = DiaryDto(id, title, date, weather, place, music, story)
//                diarys.add(dto)
//            }
//        }
//
//        cursor.close()      // cursor 사용을 종료 close()
//        helper.close()      // DB 사용이 끝 close()
//
//        return diarys
//    }
//
//
//    /*레코드 삭제 후 삭제된 레코드 개수 반환*/
//    fun deleteDiary(id: Int) : Int {
//        val helper = DiaryDBHelper(this)
//        val db = helper.writableDatabase
//
//        val whereClause = "${BaseColumns._ID}=?"
//        val whereArgs = arrayOf(id.toString())
//
//        val result = db.delete(DiaryDBHelper.TABLE_NAME, whereClause, whereArgs)
//
//        helper.close()
//        return result
//    }
//
//}