/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.ui.danger.DangerActivity

class MessagingService : FirebaseMessagingService() {
    private val TAG: String = MessagingService::class.java.simpleName

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        Log.d(TAG, "onMessageReceived: ${remote.notification?.title}")
        Log.d(TAG, "onMessageReceived: ${remote.notification?.body}")
        showNotification(remote.notification?.title!!, remote.notification?.body!!)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "Channel Danger"
        val channelName = "Danger Channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val intent = Intent(applicationContext, DangerActivity::class.java).apply { flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_map)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(ContextCompat.getColor(applicationContext, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(1, notification)
    }
}