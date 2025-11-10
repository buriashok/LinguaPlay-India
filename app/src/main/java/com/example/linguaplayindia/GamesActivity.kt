package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        findViewById<Button>(R.id.btnGapFill).setOnClickListener {
            startActivity(Intent(this, GameGapFillActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        findViewById<Button>(R.id.btnSentenceBuilder).setOnClickListener {
            startActivity(Intent(this, GameSentenceBuilderActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        findViewById<Button>(R.id.btnGrammarTapper).setOnClickListener {
            startActivity(Intent(this, GameGrammarTapperActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        findViewById<Button>(R.id.btnErrorSpotter).setOnClickListener {
            startActivity(Intent(this, GameErrorSpotterActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
