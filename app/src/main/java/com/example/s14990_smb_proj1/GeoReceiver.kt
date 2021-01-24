package com.example.s14990_smb_proj1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {

    private var id=1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        id+=1
        val event = GeofencingEvent.fromIntent(intent)
        for(geo in event.triggeringGeofences){
            val geo_id=geo.requestId.split("____").last()
            Log.i("location","Enter received $geo_id")
            val Shop=DatabaseRef.getById(geo_id)

            val context=GeoFactory.Get_context()
            createChannel(context)

            if(Shop !=null) {
                val n = NotificationCompat.Builder(context, "ShopId")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Welcome to Shop ${Shop.Name}")
                        .setContentText("Here you can buy ${Shop.Desc}")
                        .setAutoCancel(true)
                        .build()
                NotificationManagerCompat.from(context).notify(id, n)
            }
            else{
                val n = NotificationCompat.Builder(context, "ShopId")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Welcome to New Shop")
                        .setAutoCancel(true)
                        .build()
                NotificationManagerCompat.from(context).notify(id, n)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val c = NotificationChannel(
            "11","Enter Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(c)
    }
}