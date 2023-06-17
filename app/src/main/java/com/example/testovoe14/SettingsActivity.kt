package com.example.testovoe14

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide

class SettingsActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var buttonClear: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        imageViewFon2 = findViewById(R.id.imageViewFon2)
        buttonClear = findViewById(R.id.buttonClear)
        Glide.with(this)
            .load("http://135.181.248.237/14/fon2.png")
            .into(imageViewFon2)

        buttonClear.setOnClickListener {
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            editor.putInt("score", 0)
            for (i in 0 .. 9) {
                editor.putBoolean("buy_$i", false)
            }
            editor.apply()
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
        return super.onKeyDown(keyCode, event)
    }
}