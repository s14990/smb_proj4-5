package com.example.s14990_smb_proj1

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ShopItemDao {

    @Query("Select * from ShopItem")
    fun getShopItems() : LiveData<List<ShopItem>>

    @Insert
    fun insert(shopitem: ShopItem) : Long

    @Update
    fun update(shopitem: ShopItem)

    @Delete
    fun delete(shopitem: ShopItem)


    @Query("Delete FROM ShopItem")
    fun deleteAll()

    @Query("Select * from ShopItem")
    fun getAll() : Cursor

    @Query("DELETE FROM ShopItem WHERE _ID = :id ")
    fun delete_by_id(id: Long): Int

    @Query("Select * from ShopItem WHERE _ID = :id")
    fun get_by_id(id: Long): Cursor

}