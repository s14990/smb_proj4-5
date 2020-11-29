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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s14990_smb_proj1.databinding.ActivityMainBinding

private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            changeColor(checkedId)
        }



        val viewModel = ShopItemViewModel(application)
        val adapter = ItemsAdapter(viewModel)
        viewModel.items.observe(this, Observer{ items ->
            items.let {
                adapter.setItems(it)
            }
        })
        val itemsList=binding.itemsView
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        itemsList.adapter = adapter


        binding.addItemButton.setOnClickListener {
            viewModel.insert( ShopItem(
                itemName = binding.itemNameField.text.toString(),
                itemPrice = binding.itemPriceField.text.toString().toFloat(),
                checked = binding.itemCheckedField.isChecked
            ))
        }




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
        val editor = sp.edit()
        editor.putInt("check_id", check_id)
        editor.apply()
    }

    override fun onStart() {
        super.onStart()

        binding.radioGroup.check(sp.getInt("check_id", 0))
        binding.shopName.setText(sp.getString("shop_name", "Default shop"))

    }

    override fun onStop() {
        super.onStop()
        val editor = sp.edit()
        editor.putString("shop_name", binding.shopName.text.toString())
        editor.apply()
    }



}