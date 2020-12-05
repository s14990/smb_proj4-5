package com.example.s14990_smb_proj1

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ShopItem (@PrimaryKey(autoGenerate = true) var _ID: Long = 0, var itemName: String, var itemPrice: Float, var checked: Boolean) {}