package com.example.s14990_smb_proj1

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s14990_smb_proj1.databinding.ActivityShopListBinding

private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityShopListBinding

class ShopListActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ShopViewModel(application)
        val adapter = ShopAdapter(viewModel)
        viewModel.items.observe(this, Observer{ shops ->
            shops.let {
                adapter.setItems(it)
            }
        })
        val shopsList=binding.ShopView
        shopsList.layoutManager = LinearLayoutManager(this)
        shopsList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        shopsList.adapter = adapter


    }
}