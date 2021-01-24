package com.example.s14990_smb_proj1

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.coroutines.runBlocking

private lateinit var ShopsRef: DatabaseReference

@RequiresApi(Build.VERSION_CODES.O)
class ShopViewModel(application: Application) : AndroidViewModel(application) {
    val items = MutableLiveData<MutableList<Shop>>()


    init {
        ShopsRef = DatabaseRef.getInstance()
        refresh()
    }

    fun insert(name: String, Desc: String, Rad: Float, Lt: Double, Lg: Double) {
        val new_shop_key = ShopsRef.push().key
        ShopsRef.child(new_shop_key!!).setValue(Shop(
                Id = new_shop_key,
                Name = name,
                Desc = Desc,
                Rad = Rad,
                Lat  = Lt,
                Lon = Lg
        ))
    }

    fun delete(id: String) {
        ShopsRef.child(id).removeValue()
    }




    private fun refresh() {

        ShopsRef.get().addOnCompleteListener(object : OnCompleteListener<DataSnapshot> {
            override fun onComplete(p0: Task<DataSnapshot>) {
                runBlocking {
                    val list = mutableListOf<Shop>()
                    for (data in p0.result?.children!!) {
                        if (data.key != "1") {
                            val s=data
                            val shop = data.getValue(Shop::class.java)
                            list.add(shop as Shop)
                        }
                    }
                    items.postValue(list);
                }
            }
        })

        ShopsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                runBlocking {
                    if (snapshot.key != "1" && items.value !== null) {
                        val list = mutableListOf<Shop>()
                        val item = snapshot.getValue(Shop::class.java)
                        if (!items.value!!.contains(item)) {
                            list.addAll(items.value!!)
                            list.add(item!!)
                            items.value = list
                        }
                    }
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
                runBlocking {
                    val list = mutableListOf<Shop>()
                    val item = snapshot.getValue(Shop::class.java)
                    list.addAll(items.value!!)
                    list.remove(item)
                    items.value = list
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

}

