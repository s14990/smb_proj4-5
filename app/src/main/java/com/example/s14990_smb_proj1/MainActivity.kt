package com.example.s14990_smb_proj1

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s14990_smb_proj1.databinding.ActivityMainBinding

private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
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
            val name=binding.itemNameField.text.toString()
            val priceString=binding.itemPriceField.text.toString()
            if (name.isEmpty() || priceString.isEmpty()){
                Toast.makeText(this, "Nazwa i cena nie mogą być puste",Toast.LENGTH_SHORT).show()
            }
            else {
                val cv = ContentValues()
                cv.put(ItemsProvider.ITEM_NAME, name)
                cv.put(ItemsProvider.ITEM_PRICE,  priceString.toFloat())
                cv.put(ItemsProvider.CHECKED, binding.itemCheckedField.isChecked)
                viewModel.insert(cv)
            }
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