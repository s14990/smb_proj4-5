package com.example.s14990_smb_proj1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.s14990_smb_proj1.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: LoginActivityBinding
private lateinit var auth: FirebaseAuth


class LoginActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val MainIntent = Intent(this,MainActivity::class.java)

        binding.logInButton.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.userName.text.toString(), binding.userPassword.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "LogIn Successful", Toast.LENGTH_SHORT).show()
                        MainIntent.putExtra("UserName",auth.currentUser?.displayName)
                        MainIntent.putExtra("UID",auth.currentUser?.uid)
                        startActivity(MainIntent)
                    } else {
                        Toast.makeText(this, "LogIn Error ${it.exception.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.userName.text.toString(), binding.userPassword.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "SignUp Fail ${it.exception.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }

        }


    }
}