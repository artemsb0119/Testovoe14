package com.example.testovoe14

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class MenuActivity : AppCompatActivity() {

    private lateinit var buttonEarn: AppCompatButton
    private lateinit var buttonBuy: AppCompatButton
    private lateinit var buttonSale: AppCompatButton
    private lateinit var buttonSettings: AppCompatButton
    private lateinit var textViewKolvo: TextView

    private lateinit var imageViewFon2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonEarn = findViewById(R.id.buttonEarn)
        buttonBuy = findViewById(R.id.buttonBuy)
        buttonSale = findViewById(R.id.buttonSale)
        buttonSettings = findViewById(R.id.buttonSettings)
        textViewKolvo = findViewById(R.id.textViewKolvo)
        imageViewFon2 = findViewById(R.id.imageViewFon2)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        textViewKolvo.text= sharedPreferences.getInt("score",0).toString()

        Glide.with(this)
            .load("http://135.181.248.237/14/fon2.png")
            .into(imageViewFon2)

        buttonEarn.setOnClickListener {
            val intent = Intent(this, EarnActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonBuy.setOnClickListener {
            val intent = Intent(this, BuyActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonSale.setOnClickListener {
            val intent = Intent(this, SaleActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}