package ddwu.com.mobile.movieapp.data

data class PlaceRoot (
    val items: List<Item>,
)


data class Item (
        val title: String,
        val description: String,
        val address: String,
        val roadAddress: String,
    )