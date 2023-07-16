package com.attackervedo.gallarypractice

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.gallarypractice.databinding.ActivityFrameBinding
import com.attackervedo.gallarypractice.databinding.ItemFrameBinding

class FrameAdapter(private val list: List<FrameItem>) : RecyclerView.Adapter<FrameViewHodler>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHodler {
       val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
       val binding = ItemFrameBinding.inflate(inflater,parent,false)
        return FrameViewHodler(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: FrameViewHodler, position: Int) {
    holder.bind(list[position])
    }
}


data class FrameItem(
    val uri: Uri
)
class FrameViewHodler(private val binding: ItemFrameBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(item: FrameItem){
    binding.frameImageView.setImageURI(item.uri)
    }
}