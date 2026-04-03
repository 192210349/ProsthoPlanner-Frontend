package com.simats.prosthoplanner.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.simats.prosthoplanner.R
import java.text.SimpleDateFormat
import java.util.*

object NotificationHelper {
    private const val CHANNEL_ID = "ai_updates"

    fun showNotification(context: Context, title: String, message: String) {
        // 1. Show Push Notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "AI Analysis Updates", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        // 2. Persist Notification for History
        saveNotificationToHistory(context, title, message)
    }

    private fun saveNotificationToHistory(context: Context, title: String, message: String) {
        val prefs = context.getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("NOTIFICATION_HISTORY", "[]")
        
        val type = object : TypeToken<MutableList<NotificationItem>>() {}.type
        val history: MutableList<NotificationItem> = gson.fromJson(json, type)
        
        val timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        history.add(0, NotificationItem(title, message, timestamp)) // Newest first
        
        prefs.edit().putString("NOTIFICATION_HISTORY", gson.toJson(history)).apply()
    }

    fun clearNotifications(context: Context) {
        val prefs = context.getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("NOTIFICATION_HISTORY", "[]").apply()
    }

    data class NotificationItem(val title: String, val message: String, val timestamp: String)
}
