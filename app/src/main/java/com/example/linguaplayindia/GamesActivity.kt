package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        // Back
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.btnGapFill).setOnClickListener {
            startActivity(Intent(this, GameGapFillActivity::class.java))
        }

        findViewById<TextView>(R.id.btnSentenceBuilder).setOnClickListener {
            startActivity(Intent(this, GameSentenceBuilderActivity::class.java))
        }

        findViewById<TextView>(R.id.btnGrammarTapper).setOnClickListener {
            startActivity(Intent(this, GameGrammarTapperActivity::class.java))
        }

        findViewById<TextView>(R.id.btnErrorSpotter).setOnClickListener {
            startActivity(Intent(this, GameErrorSpotterActivity::class.java))
        }
    }
}
