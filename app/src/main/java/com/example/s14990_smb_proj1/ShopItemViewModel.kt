package com.example.s14990_smb_proj1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ShopItemViewModel (application: Application) : AndroidViewModel(application) {
    private val repo: ShopItemRepo
    val items: LiveData<List<ShopItem>>

    init{
        repo = ShopItemRepo(ShopItemDB.getDatabase(application.applicationContext).shopItemDao())
        items = repo.allItems
    }

    fun insert(student: ShopItem) = repo.insert(student)

    fun update(student: ShopItem) = repo.update(student)

    fun delete(student: ShopItem) = repo.delete(student)

    fun deleteAll() = repo.deleteAll()
}