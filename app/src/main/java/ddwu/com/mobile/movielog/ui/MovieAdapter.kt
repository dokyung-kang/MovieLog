package ddwu.com.mobile.movielog.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.movielog.data.MovieList
import ddwu.com.mobile.movielog.databinding.ListMovieBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    var movies: List<MovieList>? = null

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemBinding = ListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
//        holder.itemBinding.tvItem.text = movies?.get(position).toString()
        holder.itemBinding.tvItem.text = movies?.get(position)?.movieNm.toString()
        holder.itemBinding.tvItemGenre.text = movies?.get(position)?.genreAlt.toString()
        holder.itemBinding.tvItemDate.text = movies?.get(position)?.openDt.toString()
        holder.itemBinding.root.setOnClickListener{
            itemClickListener?.onItemClickListener(it, position)
            true
        }
    }

    class MovieHolder(val itemBinding: ListMovieBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListener {
        fun onItemClickListener(view: View, pos: Int)
    }

    var itemClickListener : OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }
}