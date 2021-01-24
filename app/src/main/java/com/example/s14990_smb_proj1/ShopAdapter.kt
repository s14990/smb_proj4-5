package com.example.s14990_smb_proj1

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.s14990_smb_proj1.databinding.ShopElementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopAdapter(val shopViewModel: ShopViewModel) : RecyclerView.Adapter<ShopAdapter.MyViewHolder>() {

    private var shopList = emptyList<Shop>()

    class MyViewHolder(val binding: ShopElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShopElementBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, index: Int) {

        holder.binding.shopId.text = shopList[index].Id
        holder.binding.shopName.text = shopList[index].Name
        holder.binding.shopDesc.text = shopList[index].Desc
        holder.binding.shopLat.text = shopList[index].Lat.toString()
        holder.binding.shopLon.text = shopList[index].Lon.toString()
        holder.binding.shopRad.text = shopList[index].Rad.toString()

        holder.binding.root.setOnClickListener{
            CoroutineScope(IO).launch {
                shopViewModel.delete(shopList[index].Id)
                withContext(Main) {
                    notifyDataSetChanged()
                }
            }
        }


    }

    override fun getItemCount(): Int = shopList.size


    fun setItems(ShopList: List<Shop>){
        CoroutineScope(IO).launch {
            shopList = ShopList
            withContext(Main) {
                notifyDataSetChanged()
            }
        }
    }

}