package com.patroleum.jesusandgod

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private var splashComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.supportActionBar?.hide()

        Thread {
            Thread.sleep(SPLASH_DURATION)
            splashComplete = true
            launch()
        }.start()
    }

    override fun onResume() {
        super.onResume()
        launch()
    }

    private fun launch() {
        if (splashComplete) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}