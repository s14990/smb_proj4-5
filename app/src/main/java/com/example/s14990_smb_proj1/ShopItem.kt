package com.example.s14990_smb_proj1

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class ShopItem (var _ID: String = "1", var itemName: String, var itemPrice: Float, var itemPCount: Int, var checked: Boolean) {

    constructor(): this("1","A", 1F,1,true)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "_ID" to _ID,
                "itemName" to itemName,
                "itemPrice" to itemPrice,
                "itemPCount" to itemPCount,
                "checked" to checked
        )
    }
}