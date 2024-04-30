package com.app.myapplication.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.myapplication.databinding.RowNewsItemBinding
import com.app.myapplication.listener.NewsClickListener
import com.app.myapplication.model.ArticlesItem

class HomeAdapter(val arrayList: ArrayList<ArticlesItem>, val clickListener: NewsClickListener) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    fun setData(arrayList: ArrayList<ArticlesItem>) {
        this.arrayList.clear()
        this.arrayList.addAll(arrayList)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(val binding: RowNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ArticlesItem) {
            binding.model = data
            binding.imgNew.load(data.urlToImage)

            binding.root.setOnClickListener {
                clickListener.onNewsClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            RowNewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

}