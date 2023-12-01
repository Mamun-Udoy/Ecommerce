package com.example.ecommerce.ui.home_product_item.db

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProductDetailsBinding
import com.example.ecommerce.ui.check_out_item.CheckOutItem
import com.example.ecommerce.ui.check_out_item.CheckOutItemInsertViewModel
import com.example.ecommerce.ui.check_out_item.CheckOutViewModel
import com.example.ecommerce.ui.home_product_item.network_retrofit.RetrofitDataModel
import com.example.ecommerce.ui.home_product_item.network_retrofit.RetrofitViewModel
import com.google.gson.Gson
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var dataItem: RetrofitDataModel.Product

    private val viewModel by viewModels<CheckOutViewModel>()

    private val viewModelCheckOutItemInsertViewModel by activityViewModels<CheckOutItemInsertViewModel>() // sharedViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            val dataStr = arguments?.getString("data_item")
            dataItem = Gson().fromJson(dataStr, RetrofitDataModel.Product::class.java)
            Log.d("check_item", "check item value $dataStr")
        }


        val carousel: ImageCarousel = binding.carousel
        carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()



        for (item in dataItem.images!!) {
            list.add(
                CarouselItem(
                    imageUrl = item,
                )
            )
        }
        carousel.setData(list)

        binding.discount.text = dataItem.discount.toString()
        binding.brand.text = dataItem.brand.toString()
        binding.categories.text = dataItem.categories.toString()
        binding.stock.text = dataItem.stock.toString()
        binding.rating.text = dataItem.rating.toString()


        binding.checkout.setOnClickListener {

            Toast.makeText(context, "clicked checkout", Toast.LENGTH_SHORT).show()

            //inserting checkout item into database
            viewModel.insertCheckoutItem(
                checkOutItem = getCheckoutItem(dataItem),
                context = requireContext()
            )
            val updatedSize = context?.let { it1 -> viewModel.getCheckoutItemsSize(it1) }
            if (updatedSize != null) {
                viewModelCheckOutItemInsertViewModel.updateDatabaseSize(updatedSize)
            }

        }


    }

    private fun getCheckoutItem(item: RetrofitDataModel.Product): CheckOutItem {
        return CheckOutItem(
            itemId = item.id,
            discount = item.discount.toFloat(),
            rating = item.rating.toFloat(),
            stock = item.stock,
            brand = item.brand,
            category = item.categories,
        )
    }


}