package com.example.s14990_smb_proj1

import android.content.ContentValues
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

        holder.binding.elemId.text = shopItemsList[index]._ID
        holder.binding.elemName.setText(shopItemsList[index].itemName)
        holder.binding.elemPrice.setText(shopItemsList[index].itemPrice.toString())
        holder.binding.elemCount.setText(shopItemsList[index].itemPCount.toString())
        holder.binding.elemChecked.isChecked = shopItemsList[index].checked
        holder.binding.elemUpdate.visibility=INVISIBLE


        holder.binding.root.setOnClickListener{
            itemsViewModel.delete(shopItemsList[index]._ID)
            notifyDataSetChanged()
        }

        holder.binding.elemChecked.setOnClickListener {
            holder.binding.elemUpdate.visibility= VISIBLE
        }

        holder.binding.elemUpdate.setOnClickListener {
            val nameS = holder.binding.elemName.text.toString()
            val priceS = holder.binding.elemPrice.text.toString()
            val countS = holder.binding.elemCount.text.toString()

            if(nameS.isEmpty() || priceS.isEmpty() || countS.isEmpty()) {
                Toast.makeText(holder.binding.root.context, "Nazwa,cena i ilość nie mogą być puste",Toast.LENGTH_SHORT).show()
            }
            else{
                shopItemsList[index].checked = holder.binding.elemChecked.isChecked
                shopItemsList[index].itemName = nameS
                shopItemsList[index].itemPrice = priceS.toFloat()
                shopItemsList[index].itemPCount = countS.toInt()
                val item = shopItemsList[index]
                itemsViewModel.update(shopItemsList[index])
                holder.binding.elemUpdate.visibility= INVISIBLE
                //notifyDataSetChanged()
            }
        }


        holder.binding.elemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                holder.binding.elemUpdate.visibility= VISIBLE
            }
        })

        holder.binding.elemPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                holder.binding.elemUpdate.visibility= VISIBLE
            }
        })

        holder.binding.elemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                holder.binding.elemUpdate.visibility= VISIBLE
            }
        })



    }

    override fun getItemCount(): Int = shopItemsList.size


    fun setItems(itemsList: List<ShopItem>){
        shopItemsList = itemsList
        notifyDataSetChanged()
    }

}