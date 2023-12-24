package ddwu.com.mobile.movieapp.data

data class MovieRoot (
    val items: List<Item>,
)

data class Item (
    val title: String,
    val image: String,
)