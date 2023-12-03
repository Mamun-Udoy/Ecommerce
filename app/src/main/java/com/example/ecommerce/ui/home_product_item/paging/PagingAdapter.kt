package com.example.ecommerce.ui.home_product_item.paging

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ProductItemBinding
import com.example.ecommerce.ui.home_product_item.db.entity.ProductEntity
import com.example.ecommerce.ui.home_product_item.network_retrofit.RetrofitDataModel

class PagingAdapter(
    private val cacheInData: CacheInData, private val callback: ItemClickCallback
) :
    PagingDataAdapter<RetrofitDataModel.Product, PagingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private val dataListUpdated: ArrayList<ProductEntity> = arrayListOf()




    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RetrofitDataModel.Product>() {
            override fun areItemsTheSame(
                oldItem: RetrofitDataModel.Product,
                newItem: RetrofitDataModel.Product
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RetrofitDataModel.Product,
                newItem: RetrofitDataModel.Product
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.data = item

        holder.binding.price.text = item?.price.toString()
        holder.binding.price.paintFlags = holder.binding.price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.binding.discountedPrice.text =item?.discount.toString()

        cacheInData.cacheData(item)

        if (item != null) {
            Glide.with(holder.binding.image.context)
                .load("${item.thumbnail}")
                .into(holder.binding.image)
        }


        holder.binding.button.setOnClickListener {
            if (item != null) {
                callback.onItemClicked(item)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)

    }

    interface CacheInData {
        fun cacheData(item: RetrofitDataModel.Product?)
    }

    fun updateList(items: List<ProductEntity>) {
        dataListUpdated.clear()
        dataListUpdated.addAll(items)
        notifyDataSetChanged()
    }

    interface ItemClickCallback {
        fun onItemClicked(item: RetrofitDataModel.Product)
    }


}