package ddwu.com.mobile.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.movieapp.data.Diary
import ddwu.com.mobile.movieapp.databinding.ListDetailBinding

class DetailAdapter(val diarys: List<Diary>) : RecyclerView.Adapter<DetailAdapter.DiaryViewHolder>() {
    val TAG = "DiaryAdapter"

    override fun getItemCount(): Int {
        return diarys.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val itemBinding = ListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.itemBinding.tvDIary.text = diarys[position].toString()
        holder.itemBinding.root.setOnLongClickListener{
            itemLongClickListener?.onItemLongClickListener(it, position)
            true
        }
    }

    class DiaryViewHolder(val itemBinding: ListDetailBinding)
        : RecyclerView.ViewHolder(itemBinding.root)


    interface OnItemLongClickListener {
        fun onItemLongClickListener(view: View, pos: Int)
    }

    var itemLongClickListener : OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        itemLongClickListener = listener
    }

}