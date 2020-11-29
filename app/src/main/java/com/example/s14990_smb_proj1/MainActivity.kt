package com.example.s14990_smb_proj1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.s14990_smb_proj1.databinding.ActivityMainBinding

private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            changeColor(checkedId)
        }



        setContentView(binding.root)
    }


    private fun changeColor(check_id: Int) {
        if (binding.radioDark.id == check_id){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
            Toast.makeText(applicationContext, "Dark theme", Toast.LENGTH_SHORT).show()
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
            Toast.makeText(applicationContext, "Light Theme", Toast.LENGTH_SHORT).show()
        }
        val editor = sp.edit();
        editor.putInt("check_id", check_id)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()

        binding.radioGroup.check(sp.getInt("check_id", binding.radioLight.id))
        binding.shopName.setText(sp.getString("shop_name", "Default shop"))

    }

    override fun onStop() {
        super.onStop()
        val editor = sp.edit();
        editor.putString("shop_name", binding.shopName.text.toString())
        editor.apply()
    }



}