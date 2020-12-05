package com.example.s14990_smb_proj1

import android.content.ContentValues
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.s14990_smb_proj1.databinding.ListElementBinding

class ItemsAdapter(val itemsViewModel: ShopItemViewModel) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    private var shopItemsList = emptyList<ShopItem>()

    class MyViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListElementBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, index: Int) {

        holder.binding.elemId.text = shopItemsList[index]._ID.toString()
        holder.binding.elemName.text = shopItemsList[index].itemName
        holder.binding.elemPrice.text = shopItemsList[index].itemPrice.toString()
        holder.binding.elemChecked.isChecked = shopItemsList[index].checked

        holder.binding.root.setOnClickListener{
            itemsViewModel.delete(shopItemsList[index]._ID)
            notifyDataSetChanged()
        }
        holder.binding.elemChecked.setOnClickListener {
            shopItemsList[index].checked = holder.binding.elemChecked.isChecked
            var item = shopItemsList[index]

            var cv = ContentValues()
            cv.put(ItemsProvider._ID,item._ID)
            cv.put(ItemsProvider.ITEM_NAME,item.itemName)
            cv.put(ItemsProvider.ITEM_PRICE,item.itemPrice)
            cv.put(ItemsProvider.CHECKED,item.checked)
            itemsViewModel.update(cv)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = shopItemsList.size


    fun setItems(itemsList: List<ShopItem>){
        this.shopItemsList = itemsList
        notifyDataSetChanged()
    }
}