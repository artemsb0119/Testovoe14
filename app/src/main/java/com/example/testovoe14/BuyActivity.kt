package com.example.testovoe14

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class BuyActivity : AppCompatActivity() {

    private lateinit var activity: Activity
    private lateinit var imageViewFon2: ImageView
    private lateinit var crypts: List<Crypta>
    private lateinit var textViewKolvo: TextView

    private var score = 0
    private lateinit var rv_table: RecyclerView
    private lateinit var adapter: BuyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        rv_table = findViewById(R.id.rv_table)
        rv_table.layoutManager = LinearLayoutManager(this)

        textViewKolvo = findViewById(R.id.textViewKolvo)
        imageViewFon2 = findViewById(R.id.imageViewFon2)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        score= sharedPreferences.getInt("score",0)
        textViewKolvo.text = score.toString()

        Glide.with(this)
            .load("http://135.181.248.237/14/fon2.png")
            .into(imageViewFon2)

        activity = this
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val data = URL("http://135.181.248.237/14/buy.json").readText()
                val gson = Gson()
                crypts = gson.fromJson(data, Array<Crypta>::class.java).toList()
                val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                for (i in 0 until crypts.size) {
                    val isBought = sharedPreferences.getBoolean("buy_$i", false)
                    crypts[i].isBought = isBought
                }

                activity.runOnUiThread {
                    adapter = BuyAdapter(crypts, score)
                    rv_table.adapter = adapter
                    adapter.setOnItemClickListener(object : BuyAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val crypta = adapter.getItem(position)
                            if (crypta.price<=score) {
                                score -= crypta.price
                                textViewKolvo.text = score.toString()
                                val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
                                editor.putInt("score", score)
                                editor.putBoolean("buy_$position", true)
                                editor.apply()
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
        return super.onKeyDown(keyCode, event)
    }
}