package ddwu.com.mobile.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ddwu.com.mobile.movieapp.data.Item
import ddwu.com.mobile.movieapp.databinding.ListPlaceBinding

class PlaceAdapter: RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {
    var places: List<Item>? = null

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val itemBinding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        holder.itemBinding.pTitle.text = places?.get(position)?.title.toString()
        holder.itemBinding.pLaod.text = places?.get(position)?.roadAddress.toString()
        holder.itemBinding.pDes.text = places?.get(position)?.link.toString()
        holder.itemBinding.pItem.setOnClickListener{
            clickListener?.onItemClick(it, position)
        }
    }

    class PlaceHolder(val itemBinding: ListPlaceBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: OnItemClickListner) {
        this.clickListener = listener
    }

}