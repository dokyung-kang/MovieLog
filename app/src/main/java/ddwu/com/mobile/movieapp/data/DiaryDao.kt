package ddwu.com.mobile.movieapp.data

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

//    @Update
//    suspend fun updateFood(food : Food)
    @Query("UPDATE diary_table SET dContent = :dContent WHERE title = :title")
    suspend fun updateDiary(title: String, dContent: String)

//    @Delete
//    suspend fun deleteFood(food : Food)
    @Query("DELETE FROM diary_table WHERE title = :title")
    suspend fun deleteDiay(title: String)
}