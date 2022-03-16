package com.hoang.wastenot


import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.hoang.wastenot.databinding.NotificationsFragmentBinding


class Notifications : AppCompatActivity() {

    private val CHANNEL_ID = "EXPIRY"

    private lateinit var binding: NotificationsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationsFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.stat_notify_chat)
            .setContentTitle("Your ingredient is expiring soon!")
            .setContentText("Check suggested recipes")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
    }



    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Expiry Notification"
            val descriptionText = "Receive reminders for expiring goods"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            }
        }


    }







