package ddwu.com.mobile.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.movieapp.data.MovieList
import ddwu.com.mobile.movieapp.databinding.ListItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    var movies: List<MovieList>? = null

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.itemBinding.tvItem.text = movies?.get(position).toString()
    }

    class MovieHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}