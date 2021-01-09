package com.example.s14990_smb_proj1

import android.content.*
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.IntentCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s14990_smb_proj1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


private lateinit var sp: SharedPreferences
private lateinit var binding: ActivityMainBinding
private lateinit var UID: String

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        sp = getPreferences(Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UID = intent.getStringExtra("UID").toString()

        binding.defaultCount.filters= arrayOf<InputFilter>(object : InputFilter {
            override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                val new_value = dest.toString().substring(0, dstart) + source.subSequence(start, end).toString()
                if (new_value.isEmpty())
                    return "1"
                else return null
            }
        })

        binding.defaultCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.itemCountField.setText(s.toString())

            }
        })


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            changeColor(checkedId)
        }

        val viewModel = ShopItemViewModel(application, UID)
        val adapter = ItemsAdapter(viewModel)
        viewModel.items.observe(this, Observer { items ->
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
            val countString=binding.itemCountField.text.toString()
            if (name.isEmpty() || priceString.isEmpty() || countString.isEmpty()){
                Toast.makeText(this, "Nazwa,cena i ilość nie mogą być puste", Toast.LENGTH_SHORT).show()
            }
            else {
                CoroutineScope(IO).launch {
                    viewModel.insert(name, priceString.toFloat(), countString.toInt(), binding.itemCheckedField.isChecked)
                }
                binding.itemNameField.setText("")
                binding.itemPriceField.setText("")
                binding.itemCountField.setText(binding.defaultCount.text.toString())
            }
        }
        binding.SwitchButton.setOnClickListener {
            val Main2Intent = Intent(this, MainActivity2::class.java)
            Main2Intent.putExtra("UID", UID)
            Main2Intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(Main2Intent)
            finish()
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
        binding.defaultCount.setText(sp.getString("default_count", "1"))

    }

    override fun onStop() {
        super.onStop()
        val editor = sp.edit()
        editor.putString("default_count", binding.defaultCount.text.toString())
        editor.apply()
    }



}