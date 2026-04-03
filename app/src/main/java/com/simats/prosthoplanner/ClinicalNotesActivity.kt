package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ClinicalNotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinical_notes)

        findViewById<androidx.cardview.widget.CardView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddNote).setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        // Connect static notes for demo
        findViewById<androidx.cardview.widget.CardView>(R.id.note1).setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("NOTE_DATE", "Oct 20, 2023 | 10:30 AM")
                putExtra("NOTE_CONTENT", "Initial diagnostic impressions completed. Patient shows good systemic health but expresses anxiety regarding the surgical phase.")
            }
            startActivity(intent)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.note2).setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra("NOTE_DATE", "Oct 22, 2023 | 02:15 PM")
                putExtra("NOTE_CONTENT", "AI data validated against physical measurements. High success probability confirmed for Site #24.")
            }
            startActivity(intent)
        }
    }
}
