package com.example.s14990_smb_proj1

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LiveData

class ItemsProvider () : ContentProvider() {

    lateinit var dao: ShopItemDao

    companion object{
        const val PROVIDER_NAME ="com.example.s14990_smb_proj1/ItemsProvider"
        private const val URL = "content://$PROVIDER_NAME/SHOPITEM"

        val PROVIDER_URI: Uri = Uri.parse(URL)

        const val _ID="_ID"
        const val ITEM_NAME="itemName"
        const val ITEM_PRICE="itemPrice"
        const val CHECKED="checked"
    }

    override fun onCreate(): Boolean {
        dao = ShopItemDB.getDatabase(this.context!!.applicationContext).shopItemDao()
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (values != null){
            val id= dao.insert(fromContentValues(values))
            context!!.contentResolver.notifyChange(uri, null)
        }
        return uri
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        var cursor = dao.getAll()
        cursor.setNotificationUri(context!!.contentResolver, uri);
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        if (values != null){
           dao.update(fromContentValues(values))
            context!!.contentResolver.notifyChange(uri, null)
            return 1
        }
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted = dao.delete_by_id(ContentUris.parseId(uri))
        context!!.contentResolver.notifyChange(uri, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }


    private fun fromContentValues(contentValues: ContentValues): ShopItem {
        if (contentValues.containsKey(_ID)) {
                return ShopItem(
                        _ID = contentValues.getAsLong(_ID),
                        itemName = contentValues.getAsString(ITEM_NAME),
                        itemPrice = contentValues.getAsFloat(ITEM_PRICE),
                        checked = contentValues.getAsBoolean( CHECKED)
                )
        }
            return ShopItem(
                    itemName = contentValues.getAsString(ITEM_NAME),
                    itemPrice = contentValues.getAsFloat(ITEM_PRICE),
                    checked = contentValues.getAsBoolean( CHECKED)
            )
    }

}