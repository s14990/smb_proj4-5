package com.example.s14990_smb_proj1

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ShopItemDao {

    @Query("Select * from ShopItem")
    fun getShopItems() : LiveData<List<ShopItem>>

    @Insert
    fun insert(student: ShopItem)

    @Update
    fun update(student: ShopItem)

    @Delete
    fun delete(student: ShopItem)

    @Query("Delete FROM ShopItem")
    fun deleteAll()

}