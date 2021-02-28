package com.example.s14990_smb_proj1

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object DatabaseRef {
    private var instance: DatabaseReference = FirebaseDatabase.getInstance().getReference("Shops")
    private var list= mutableListOf<Shop>()


    init{
    /*
        instance.get().addOnCompleteListener(object : OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onComplete(p0: Task<DataSnapshot>) {
                for (data in p0.result?.children!!) {
                    if (data.key != "1") {
                        val shop = data.getValue(Shop::class.java)
                        if (shop != null) {
                            GeoFactory.PutMarker(shop.Lat, shop.Lon, shop.Name)
                            GeoFactory.REnterGeoFence(shop.Id,shop.Lat,shop.Lon,shop.Rad)
                            GeoFactory.RExitGeoFence(shop.Id,shop.Lat,shop.Lon,shop.Rad)
                            list.add(shop as Shop)
                        }

                    }
                }
            }
        })
*/

        instance.addChildEventListener(object : ChildEventListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                runBlocking {
                    if (snapshot.key != "1") {
                        val shop = snapshot.getValue(Shop::class.java)
                        if (!list.contains(shop)) {
                            list.add(shop as Shop)
                            GeoFactory.PutMarker(shop.Lat, shop.Lon, shop.Name)
                            GeoFactory.REnterGeoFence(shop.Id,shop.Lat,shop.Lon,shop.Rad)
                            GeoFactory.RExitGeoFence(shop.Id,shop.Lat,shop.Lon,shop.Rad)
                            list.add(shop)
                        }
                    }
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                runBlocking {
                    val item = snapshot.getValue(Shop::class.java)
                    item?.Name?.let { GeoFactory.removeMarker(it) }
                    list.remove(item)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

    }
    fun getInstance(): DatabaseReference {
        return instance
    }

    fun getById(id: String): Shop? {
        return list.firstOrNull { it.Id == id }
    }

}