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

private lateinit var FD: FirebaseDatabase
private lateinit var ItemRef: DatabaseReference
@RequiresApi(Build.VERSION_CODES.O)
class ShopItemViewModel(application: Application, var User_ID: String) : AndroidViewModel(application) {
    val items = MutableLiveData<MutableList<ShopItem>>()


    class myObserver(val shopItemViewModel: ShopItemViewModel) : ContentObserver(null){
        override fun onChange(selfChange: Boolean) {
            //shopItemViewModel.refresh()
        }
    }

    init{
        FD=FirebaseDatabase.getInstance()
        ItemRef=FD.getReference("users/$User_ID")
        refresh()
    }

    fun insert(name: String,price: Float,count: Int, c: Boolean) {
        val new_item_key=ItemRef.push().key
        ItemRef.child(new_item_key!!).setValue(ShopItem(
                _ID = new_item_key,
                itemName = name,
                itemPrice = price,
                itemPCount = count,
                checked = c
        ))
    }

    fun update(item: ShopItem){
        ItemRef.child(item._ID).setValue(item)
    }

    fun delete(id: String) {

        ItemRef.child(id).removeValue()
        //val id_URL: String = "content://${ItemsProvider.PROVIDER_NAME}/SHOPITEM/$id"
        //cR.delete(Uri.parse(id_URL),null,null)
    }


    private fun refresh(){

        /*
        ItemRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list= mutableListOf<ShopItem>()
                for(data in snapshot.children) {
                    val i2=User_ID
                    val i=data
                    val i3=1
                    val shopitem=data.getValue(ShopItem::class.java)
                    list.add(shopitem as ShopItem)
                }
                items.postValue(list);
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Read Error",error.toString())
            }

        })
        //val cursor = cR.query(ItemsProvider.PROVIDER_URI,null,null,null)
        */
        /*
        ItemRef.get().addOnCompleteListener( object : OnCompleteListener<DataSnapshot>{
            override fun onComplete(p0: Task<DataSnapshot>) {
                val list= mutableListOf<ShopItem>()
                for(data in p0.result?.children!!) {
                    val shopitem=data.getValue(ShopItem::class.java)
                    list.add(shopitem as ShopItem)
                }
                items.postValue(list);
            }
        })
*/

        val list= mutableListOf<ShopItem>()
        items.postValue(list);

        ItemRef.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                if(snapshot.key!="1"){
                    val list= mutableListOf<ShopItem>()
                    val item=snapshot.getValue(ShopItem::class.java)
                    list.addAll(items.value!!)
                    list.add(item!!)
                    items.value=list
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                val list= mutableListOf<ShopItem>()
                val item=snapshot.getValue(ShopItem::class.java)!!
                list.addAll(items.value!!)
                val old_item = list.first { it._ID== item._ID  }
                list.remove(old_item)
                list.add(item)
                items.value=list
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val list= mutableListOf<ShopItem>()
                val item=snapshot.getValue(ShopItem::class.java)
                list.addAll(items.value!!)
                list.remove(item)
                items.value=list
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        }

    }

