package com.example.s14990_smb_proj1

import android.content.ClipData
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.s14990_smb_proj1.databinding.ActivityEditItemBinding
import com.example.s14990_smb_proj1.databinding.ActivityMainBinding


private lateinit var binding: ActivityEditItemBinding
private lateinit var viewModel: ShopItemViewModel
private lateinit var item: ShopItem


class EditItemActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        viewModel = ShopItemViewModel(application)
        setContentView(binding.root)
        val id = intent.getLongExtra("_ID","0".toLong())

        val dao=ShopItemDB.getDatabase(this.applicationContext).shopItemDao()

        item = getSingleItem(dao.get_by_id(id))

        binding.editName.setText(item.itemName)
        binding.editPrice.setText(item.itemPrice.toString())
        binding.editCount.setText(item.itemPCount.toString())
        binding.itemCheck.isChecked=item.checked


        binding.editbutton.setOnClickListener {
            item.itemName= binding.editName.text.toString()
            item.itemPrice = binding.editPrice.text.toString().toFloat()
            item.itemPCount = binding.editCount.text.toString().toInt()
            item.checked = binding.itemCheck.isChecked
            dao.update(item)

            val m_intent = Intent(this, MainActivity::class.java)
            startActivity(m_intent)
        }


    }


    fun getSingleItem(cursor: Cursor): ShopItem {
        cursor?.let {
            cursor.moveToFirst()
            val indexId=cursor.getColumnIndex(ItemsProvider._ID)
            val indexName = cursor.getColumnIndex(ItemsProvider.ITEM_NAME)
            val indexPrice = cursor.getColumnIndex(ItemsProvider.ITEM_PRICE)
            val indexChecked = cursor.getColumnIndex(ItemsProvider.CHECKED)
            val indexItemCount = cursor.getColumnIndex(ItemsProvider.ITEM_COUNT)
            val newItem = ShopItem(
                _ID = cursor.getLong(indexId),
                itemName = cursor.getString(indexName),
                itemPrice = cursor.getFloat(indexPrice),
                itemPCount = cursor.getInt(indexItemCount),
                checked = cursor.getInt(indexChecked) > 0
            )
            cursor.close()
            return newItem
        }

    }



}