package com.simats.prosthoplanner

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        val date = intent.getStringExtra("NOTE_DATE") ?: "Oct 20, 2023"
        val content = intent.getStringExtra("NOTE_CONTENT") ?: "Initial diagnostic impressions completed."
        
        findViewById<TextView>(R.id.tvDate).text = date
        findViewById<TextView>(R.id.tvContent).text = content
    }
}
