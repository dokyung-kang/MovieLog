package ddwu.com.mobile.movielog.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Singleton 패턴 적용
@Database(entities = [Diary::class], version=1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao() : DiaryDao

    companion object {
        @Volatile       // Main memory 에 저장한 값 사용
        private var INSTANCE : DiaryDatabase? = null

        // INSTANCE 가 null 이 아니면 반환, null 이면 생성하여 반환
        fun getDatabase(context: Context) : DiaryDatabase {
            return INSTANCE ?: synchronized(this) {     // 단일 스레드만 접근
                val instance = Room.databaseBuilder(context.applicationContext,
                    DiaryDatabase::class.java, "diary_db").build()
                INSTANCE = instance
                instance
            }
        }
    }

}