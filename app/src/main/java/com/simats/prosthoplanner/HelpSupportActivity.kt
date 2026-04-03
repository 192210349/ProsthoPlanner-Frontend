package com.simats.prosthoplanner

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HelpSupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnSubmitSupport).setOnClickListener {
            Toast.makeText(this, "Support request sent. Our team will contact you within 24 hours.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
