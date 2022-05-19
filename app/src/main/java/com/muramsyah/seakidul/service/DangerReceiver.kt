/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.ui.danger.DangerActivity
import java.util.*

class DangerReceiver : BroadcastReceiver() {

    companion object {
        const val TRIGGERED_ID = 404
    }

    override fun onReceive(context: Context, intent: Intent) {
        val goToDangerActivity = Intent(context, DangerActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(goToDangerActivity)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun triggeredPhone(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DangerReceiver::class.java)
        val calendar = Calendar.getInstance()
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, TRIGGERED_ID, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, TRIGGERED_ID, intent, 0)
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}