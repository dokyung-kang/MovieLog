package ddwu.com.mobile.movielog.data

data class PlaceRoot (
    val items: List<Item>,
)


data class Item (
        val title: String,
        val link: String,
        val roadAddress: String,
    )