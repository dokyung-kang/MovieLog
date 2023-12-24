package ddwu.com.mobile.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val _id: Int,

    var title: String?,

    var dContent: String?
)
{
    override fun toString(): String {
        return "$_id - $title ($dContent)"
    }
}
