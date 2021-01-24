package com.example.s14990_smb_proj1;
import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Shop (var Id: String = "1", var Name: String, var Desc: String, var Rad : Float, var Lat: Double, var Lon: Double) {

    constructor(): this("1","A", "BB",100F,
            55.0,55.0)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "Id" to Id,
                "Name" to Name,
                "Desc" to Desc,
                "Rad" to Rad,
                "Lat" to Lat,
                "Lon" to Lon
        )
    }
}