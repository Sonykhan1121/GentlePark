package com.example.gentlepark.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gentlepark.data.Product
import com.example.gentlepark.databinding.BestDealsRvItemBinding
import com.example.gentlepark.databinding.FragmentMainCategoryBinding

class BestDealsAdapter :RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {
    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding): ViewHolder(binding.root){
        fun bind(product :Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageIdBestDeals)
                product.offerpercentage?.let {
                    val remainingPercentage =  1f - it
                    val priceAfterOffer  = remainingPercentage*product.price
                    offerPriceId.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                    originalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                if(product.offerpercentage ==null)
                {
                    offerPriceId.visibility = View.INVISIBLE
                }
                originalPrice.text = "$ "+product.price.toString()
                titleIdBest.text = product.name

            }
        }
    }
    private val diffCallback   = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return  oldItem == newItem
        }

    }
    val differ =  AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
       return  BestDealsViewHolder(
           BestDealsRvItemBinding.inflate(
               LayoutInflater.from(parent.context)
           )
       )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

}