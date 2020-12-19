package com.example.s14990_smb_proj1

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

@RequiresApi(Build.VERSION_CODES.O)
class ShopItemViewModel (application: Application) : AndroidViewModel(application) {
    val items = MutableLiveData<List<ShopItem>>()
    private val cR: ContentResolver


    class myObserver(val shopItemViewModel: ShopItemViewModel) : ContentObserver(null){
        override fun onChange(selfChange: Boolean) {
            shopItemViewModel.refresh()
        }
    }

    init{
        cR = application.contentResolver
        cR.registerContentObserver(ItemsProvider.PROVIDER_URI,true,myObserver(this))
        refresh()
    }

    fun insert(cv: ContentValues) : Long {
        val uri_id=cR.insert(ItemsProvider.PROVIDER_URI,cv)
        if(uri_id != null){
            return ContentUris.parseId(uri_id)
        }
        return 0
    }

    fun update(cv : ContentValues){
        cR.update(ItemsProvider.PROVIDER_URI,cv,null,null)
    }

    fun delete(id: Long) {
        val id_URL: String = "content://${ItemsProvider.PROVIDER_NAME}/SHOPITEM/$id"
        cR.delete(Uri.parse(id_URL),null,null)
    }


    private fun refresh(){
        val cursor = cR.query(ItemsProvider.PROVIDER_URI,null,null,null)
        val list= mutableListOf<ShopItem>()
        cursor?.let {
            cursor.moveToFirst()

            val indexId=cursor.getColumnIndex(ItemsProvider._ID)
            val indexName = cursor.getColumnIndex(ItemsProvider.ITEM_NAME)
            val indexPrice = cursor.getColumnIndex(ItemsProvider.ITEM_PRICE)
            val indexChecked = cursor.getColumnIndex(ItemsProvider.CHECKED)
            val indexItemCount = cursor.getColumnIndex(ItemsProvider.ITEM_COUNT)

            while (!cursor.isAfterLast) {
                list.add(ShopItem(
                        _ID = cursor.getLong(indexId),
                        itemName = cursor.getString(indexName),
                        itemPrice = cursor.getFloat(indexPrice),
                        itemPCount = cursor.getInt(indexItemCount),
                        checked = cursor.getInt(indexChecked) > 0
                ))
                cursor.moveToNext()
            }
            cursor.close()
            items.postValue(list)
        }

    }

}