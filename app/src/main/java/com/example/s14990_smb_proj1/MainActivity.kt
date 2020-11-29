package com.example.s14990_smb_proj1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
            if (binding.radioDark.id == checkedId){
                Toast.makeText(applicationContext, "Switched to dark theme", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(applicationContext, "Light Theme", Toast.LENGTH_SHORT).show()
            }


        }



        setContentView(binding.root)
    }

}