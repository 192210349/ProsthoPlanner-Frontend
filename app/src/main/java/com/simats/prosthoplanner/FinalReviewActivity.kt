package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FinalReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_review)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        setupSummary()

        findViewById<Button>(R.id.btnGenerateProposal).setOnClickListener {
            startActivity(Intent(this, PatientProposalActivity::class.java))
        }
    }

    private fun setupSummary() {
        updateItem(R.id.detail1, "Restoration Material", "Zirconia")
        updateItem(R.id.detail2, "Loading Protocol", "Immediate")
        updateItem(R.id.detail3, "Retention Type", "Screw-retained")
    }

    private fun updateItem(layoutId: Int, label: String, value: String) {
        val layout = findViewById<View>(layoutId)
        layout.findViewById<TextView>(R.id.tvLabel).text = label
        layout.findViewById<TextView>(R.id.tvValue).text = value
    }
}
