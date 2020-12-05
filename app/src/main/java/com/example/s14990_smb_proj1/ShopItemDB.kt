package com.example.s14990_smb_proj1

import android.content.Context
import androidx.room.Database
import androidx.room.*
import androidx.room.Room.databaseBuilder

@Database(entities = [ShopItem::class], version = 3)
abstract class ShopItemDB : RoomDatabase() {
    abstract fun shopItemDao(): ShopItemDao

    companion object {
        private var instance: ShopItemDB? = null

        fun getDatabase(context: Context): ShopItemDB{
            if (instance != null)
                return instance as ShopItemDB

            instance = databaseBuilder(
                context,
                ShopItemDB::class.java,
                "ShopItemDB"
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()

            return instance as ShopItemDB
        }
    }

}