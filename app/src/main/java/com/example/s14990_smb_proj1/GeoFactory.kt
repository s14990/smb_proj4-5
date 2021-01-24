package com.example.s14990_smb_proj1

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.withContext
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import kotlinx.coroutines.runBlocking
import java.util.ArrayDeque


object GeoFactory {
    private lateinit var geoClient: GeofencingClient
    private lateinit var mMap: GoogleMap
    private lateinit var context: Context
    private lateinit var geoRequest: GeofencingRequest
    private var enter_id=1
    private var exit_id=200
    private var Intentslist= ArrayDeque<PendingIntent>()
    fun Get_context(): Context {
        return context
    }


    fun SetClient(client: GeofencingClient, gmap: GoogleMap,con: Context) {
        geoClient=client
        mMap = gmap
        context=con
    }


    fun PutMarker(lat: Double, lon: Double,Name: String){
        val loc= LatLng(lat,lon)
        val mr= MarkerOptions().position(loc).title(Name)
        mMap.addMarker(mr)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    fun REnterGeoFence(id: String, Lat: Double, Lon: Double, Radius: Float){
        enter_id+=1
        checkpermission()
        val geo = Geofence.Builder()
                .setRequestId("ENTER____${id}")
                .setCircularRegion(Lat,Lon,Radius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(1000000)
                .build()

            geoRequest=GeofencingRequest.Builder()
                    .addGeofence(geo).build()

        val geoPendingIntent = PendingIntent.getBroadcast(
                context,
                enter_id,
                Intent(context,GeoReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        if(Intentslist.size>4){
            geoClient.removeGeofences(Intentslist.pop())
        }
        Intentslist.push(geoPendingIntent)
        geoClient.addGeofences(geoRequest,geoPendingIntent)
                .addOnSuccessListener {
            Log.i("GEOFENCE","ENTER REGISTERED")
        }.addOnFailureListener{
                    Log.i("GEOFENCE","ENTER FAILED${it.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    fun RExitGeoFence(id: String, Lat: Double, Lon: Double, Radius: Float){
        runBlocking {
            exit_id += 1
            checkpermission()
            val geo = Geofence.Builder()
                    .setRequestId("EXIT____${id}")
                    .setCircularRegion(Lat, Lon, Radius)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setExpirationDuration(1000000)
                    .build()

            geoRequest = GeofencingRequest.Builder()
                    .addGeofence(geo).build()

            val geoPendingIntent = PendingIntent.getBroadcast(
                    context,
                    exit_id,
                    Intent(context, GeoExitReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            if(Intentslist.size>4){
                geoClient.removeGeofences(Intentslist.pop())
            }
            Intentslist.push(geoPendingIntent)

            geoClient.addGeofences(geoRequest, geoPendingIntent)
                    .addOnSuccessListener {
                        Log.i("GEOFENCE", "EXIT REGISTERED")
                    }.addOnFailureListener {
                        Log.i("GEOFENCE", "EXIT FAILED ${it.message}")
                    }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkpermission(){
        var perm = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) { }
    }

}