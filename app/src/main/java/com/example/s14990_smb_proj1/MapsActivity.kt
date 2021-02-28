package com.example.s14990_smb_proj1

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.s14990_smb_proj1.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference

private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityMapsBinding
private lateinit var ShopsRef: DatabaseReference

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geoClient: GeofencingClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        geoClient = LocationServices.getGeofencingClient(this)
        clear_all_intents()
        GeoFactory.SetClient(geoClient,mMap,this)
        var perm = arrayOf(
           Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perm,0)
        }
        mMap.isMyLocationEnabled=true
        ShopsRef = DatabaseRef.getInstance()

        binding.listCallButton.setOnClickListener {
            val MainIntent = Intent(this,ShopListActivity::class.java)
            startActivity(MainIntent)
        }

        binding.addShopBt.setOnClickListener {
            check_perm()
            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                Log.i("location","${it.latitude} ${it.longitude}")
                val ShopName=binding.shopFormName.text.toString()
                val ShopRadius= binding.shopFormRad.text.toString().toFloat()



                val new_shop_key = ShopsRef.push().key
                ShopsRef.child(new_shop_key!!).setValue(Shop(
                        Id = new_shop_key,
                        Name = binding.shopFormName.text.toString(),
                        Desc = binding.shopFormDesc.text.toString(),
                        Rad = binding.shopFormRad.text.toString().toFloat(),
                        Lat  = it.latitude,
                        Lon = it.longitude
                ))

                /*
                GeoFactory.PutMarker(it.latitude,it.longitude,ShopName)
                GeoFactory.REnterGeoFence(new_shop_key,it.latitude,it.longitude,ShopRadius)
                GeoFactory.RExitGeoFence(new_shop_key,it.latitude,it.longitude,ShopRadius)
                */
            }
        }
    }

    override fun onDestroy() {
        clear_all_intents()
        super.onDestroy()
    }
    fun check_perm(){
        var perm = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(perm,0)
        }
    }


    fun clear_all_intents(){
        for(intent in GeoFactory.Intentslist){
            geoClient.removeGeofences(intent).addOnSuccessListener {
                Log.i("GEOFENCE", "Removed")
            }.addOnFailureListener {
                Log.i("GEOFENCE", " REmove FAILED ${it.message}")
            }
        }
    }
}