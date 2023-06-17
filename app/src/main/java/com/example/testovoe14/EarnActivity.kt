package com.example.testovoe14

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class EarnActivity : AppCompatActivity() {

    private lateinit var imageViewFon2: ImageView
    private lateinit var textViewKolvo: TextView
    private lateinit var textViewQuestion: TextView
    private lateinit var buttonTrue: AppCompatButton
    private lateinit var buttonFalse: AppCompatButton

    private lateinit var questions: List<Quiz>

    private lateinit var activity: Activity

    private var score = 0
    private var clicked: Int? = null
    private var getRightAnswerPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earn)

        buttonTrue = findViewById(R.id.buttonTrue)
        textViewKolvo = findViewById(R.id.textViewKolvo)
        textViewQuestion = findViewById(R.id.textViewQuestion)
        buttonFalse = findViewById(R.id.buttonFalse)
        imageViewFon2 = findViewById(R.id.imageViewFon2)
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        score= sharedPreferences.getInt("score",0)
        textViewKolvo.text = score.toString()

        Glide.with(this)
            .load("http://135.181.248.237/14/fon2.png")
            .into(imageViewFon2)

        buttonTrue.setOnClickListener {
            clicked = 1
            playGame()
        }
        buttonFalse.setOnClickListener {
            clicked = 2
            playGame()
        }
        activity = this
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val data = URL("http://135.181.248.237/14/quiz.json").readText()
                val gson = Gson()
                questions = gson.fromJson(data, Array<Quiz>::class.java).toList()
                activity.runOnUiThread {
                    generateQuestion()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateQuestion() {
        val a = (Math.random() * questions.size).toInt()
        textViewQuestion.text = questions[a].title
        getRightAnswerPosition = questions[a].answer
    }

    private fun playGame() {
        if (buttonTrue.isPressed || buttonFalse.isPressed) {
            val editor = getSharedPreferences("my_preferences", Context.MODE_PRIVATE).edit()
            if (clicked == getRightAnswerPosition) {
                score +=2
                textViewKolvo.text = score.toString()
                editor.putInt("score", score)
                editor.apply()
            }
            else{
                score -=1
                if(score <0) {
                    score = 0
                }
                textViewKolvo.text = score.toString()
                editor.putInt("score", score)
                editor.apply()
            }
        }

        generateQuestion()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
        return super.onKeyDown(keyCode, event)
    }
}