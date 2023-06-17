package com.example.testovoe14

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    companion object {
        private const val SERVER_URL = "http://135.181.248.237/splash.php"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var progressBar: ProgressBar
    private lateinit var imageViewFon1: ImageView
    private lateinit var textView: TextView

    private var isActivityDestroyed = false

    private lateinit var uniqueId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)
        imageViewFon1 = findViewById(R.id.imageViewFon1)

        progressBar.max = 100
        textView.text = "0"

        Glide.with(this)
            .load("http://135.181.248.237/14/fon1.png")
            .into(imageViewFon1)

        Glide.with(this)
            .load("http://135.181.248.237/14/fon2.png")
            .preload()


        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        uniqueId = sharedPreferences.getString("uniqueId", "") ?: ""

        if (uniqueId.isEmpty()) {
            uniqueId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("uniqueId", uniqueId).apply()
        }

        startNextActivity()
    }


    private fun startNextActivity() {

        val doMain = {
            val intent = Intent(this@SplashActivity, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        coroutineScope.launch {
            launchProgressBar()
            val mod = getStartMod()

            progressBar.visibility = View.GONE
            textView.visibility = View.GONE
            progressBar.progress = 100

            mod?.let { startMod ->
                when (startMod) {
                    "no" -> doMain.invoke()
                    "nopush" -> {
                        OneSignal.disablePush(true)
                        doMain.invoke()
                    }

                    else -> {
                        val intent = Intent(this@SplashActivity, WebViewActivity::class.java)
                        intent.putExtra("url", mod)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private suspend fun launchProgressBar() {
        var progressStatus = 0
        while (progressStatus < 100) {
            progressStatus += 1
            progressBar.progress = progressStatus
            textView.text = "$progressStatus"
            delay(30)
        }
    }

    private suspend fun getStartMod(): String? = withContext(Dispatchers.IO) {
        val phoneName = getPhoneName()
        val locale = Locale.getDefault().language
        val url = SERVER_URL +
                "?phone_name=$phoneName" +
                "&locale=$locale" +
                "&unique=$uniqueId"
        try {
            val response = URL(url).readText()
            if (response.isNotEmpty()) {
                val jsonResponse = JSONObject(response)
                val answer = jsonResponse.getString("url")
                return@withContext answer
            } else {
                return@withContext null
            }
        } catch (e: Throwable) {
            Log.i("SplashActivity", e.message + " " + e.localizedMessage)
            return@withContext null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityDestroyed = true
    }

    private fun getPhoneName(): String {
        val manufacturer = android.os.Build.MANUFACTURER
        val model = android.os.Build.MODEL
        return "$manufacturer $model"
    }

}