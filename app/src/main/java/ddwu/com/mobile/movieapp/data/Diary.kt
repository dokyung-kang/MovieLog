package ddwu.com.mobile.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val _id: Int,

    var title: String?,

    var movieNm: String?,

    var cimena: String?,

    var createDate: String?,

    var createTime: String?,

    var dContent: String?,
)
{
    override fun toString(): String {
        return "번호: $_id 제목: $title 영화: $movieNm 영화관: $cimena 날짜: $createDate 시간: $createTime 내용: $dContent"
    }
}
