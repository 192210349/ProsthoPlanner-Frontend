package com.simats.prosthoplanner

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
    }
}
