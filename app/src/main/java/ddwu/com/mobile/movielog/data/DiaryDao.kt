package ddwu.com.mobile.movielog.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_table")
    fun getAllDiarys() : Flow<List<Diary>>

    @Query("SELECT * FROM diary_table WHERE title = :title")
    suspend fun getDiaryByTitle(title: String) : List<Diary>

    @Insert
    suspend fun insertDiary(vararg diary : Diary)

    @Query("UPDATE diary_table SET title =:title, movieNm = :movieNm, cimena =:cimena, " +
            "createDate =:createDate, createTime =:createTime, dContent = :dContent WHERE _id = :diaryId")
    suspend fun updateDiary(diaryId: Int, title: String, movieNm: String, cimena: String,
    createDate: String, createTime: String, dContent: String)

//    @Delete
//    suspend fun deleteFood(food : Food)
    @Query("DELETE FROM diary_table WHERE _id = :diaryID")
    suspend fun deleteDiay(diaryID: Int)
}