package com.gsm.quotesmvvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gsm.quotesmvvm.databinding.QuoteItemBinding
import com.gsm.quotesmvvm.models.Result

class QuoteListAdapter(val quoteList: List<Result>) :
    RecyclerView.Adapter<QuoteListAdapter.QuoteListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuoteListViewHolder {
        val binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: QuoteListViewHolder,
        position: Int
    ) {
        val item = quoteList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = quoteList.size

    inner class QuoteListViewHolder(val binding: QuoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result) {
            binding.apply {
                tvQuote.text = item.content
                tvAuthor.text = item.author
            }
        }
    }


}