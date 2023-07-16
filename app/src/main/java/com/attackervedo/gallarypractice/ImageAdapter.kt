package com.attackervedo.gallarypractice

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.attackervedo.gallarypractice.databinding.ItemImageBinding
import com.attackervedo.gallarypractice.databinding.ItemLoadMoreBinding

class ImageAdapter(private val itemClickListener: ItemClickListener) : androidx.recyclerview.widget.ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>(){
        //같은 객체를 참조하고있는지 확인하는 부분
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem
        }
        //이퀄을 확인하는 부분(데이터가 같은지)
        // 같은 주소값인지 확인하는거, 같은 아이템인지 확인하는거 두개를 따로 구현하는것 같음 하 어렵네;
        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem
        }

    }
){

    override fun getItemCount(): Int {
        // 사진 불러오기 때문에 +1 해주는 것
        val originSize = currentList.size
        return if(originSize == 0) 0 else originSize.inc()
    }
//inc 는 +1 이고, dec 는 -1 인듯?
    override fun getItemViewType(position: Int): Int {
        return if(itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            ITEM_IMAGE ->{
                val binding = ItemImageBinding.inflate(inflater,parent,false)
                ImageViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater,parent,false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }

    interface ItemClickListener{
        fun onLoadMoreClick()
    }
    companion object{
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }

}

sealed class ImageItems{
    data class Image(
        val uri : Uri, ): ImageItems()
    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(item :ImageItems.Image){
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(binding : ItemLoadMoreBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(itemClickListener: ImageAdapter.ItemClickListener){
        itemView.setOnClickListener {
            itemClickListener.onLoadMoreClick()
        }
    }
}




