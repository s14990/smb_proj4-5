package com.example.s14990_smb_proj1

class ShopItemRepo (private val shopItemDao: ShopItemDao){

    val allItems = shopItemDao.getShopItems()

    fun insert(student: ShopItem) = shopItemDao.insert(student)

    fun update(student: ShopItem) = shopItemDao.update(student)

    fun delete(student: ShopItem) = shopItemDao.delete(student)

    fun deleteAll() = shopItemDao.deleteAll()
}