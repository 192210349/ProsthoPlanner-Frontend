package com.simats.prosthoplanner

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simats.prosthoplanner.utils.NotificationHelper

class NotificationActivity : AppCompatActivity() {

    private lateinit var rvNotifications: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var tvMarkAllRead: TextView
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        rvNotifications = findViewById(R.id.rvNotifications)
        emptyState = findViewById(R.id.emptyState)
        tvMarkAllRead = findViewById(R.id.tvMarkAllRead)

        findViewById<androidx.cardview.widget.CardView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        loadNotifications()

        tvMarkAllRead.setOnClickListener {
            NotificationHelper.clearNotifications(this)
            loadNotifications()
        }
    }

    private fun setupRecyclerView() {
        rvNotifications.layoutManager = LinearLayoutManager(this)
        adapter = NotificationAdapter(emptyList())
        rvNotifications.adapter = adapter
    }

    private fun loadNotifications() {
        val prefs = getSharedPreferences("ProsthoPrefs", MODE_PRIVATE)
        val json = prefs.getString("NOTIFICATION_HISTORY", "[]")
        val type = object : TypeToken<List<NotificationHelper.NotificationItem>>() {}.type
        val history: List<NotificationHelper.NotificationItem> = Gson().fromJson(json, type)

        if (history.isEmpty()) {
            rvNotifications.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
            tvMarkAllRead.visibility = View.GONE
        } else {
            rvNotifications.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            tvMarkAllRead.visibility = View.VISIBLE
            adapter.updateData(history)
        }
    }
}
