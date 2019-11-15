package com.example.evitegiphysample.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evitegiphysample.R
import com.example.evitegiphysample.api.ApiGif

class GifsAdapter(context: Context, private val onGifClicked:(ApiGif) -> Unit,
                  private val bottomReached:(Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private var gifList = listOf<ApiGif>()

    fun setData(list: List<ApiGif>){
        this.gifList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.gif_item, parent, false)
        return GifsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = gifList[position]
        if (position == gifList.size - 1){
            bottomReached(position)
        }
        when(holder) {
            is GifsViewHolder -> {
                holder.bind(item)
                holder.itemView.setOnClickListener {
                    onGifClicked(item)
                }
            }
        }
    }

    class GifsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val gifTitle = itemView.findViewById<TextView>(R.id.giv_title)
        private val gifPrev = itemView.findViewById<ImageView>(R.id.gif_iv)

        fun bind(item: ApiGif) {
            gifTitle.text = item.title
            Glide.with(gifPrev.context).asGif().load(item.images.preview_gif.url).into(gifPrev)
        }
    }
}