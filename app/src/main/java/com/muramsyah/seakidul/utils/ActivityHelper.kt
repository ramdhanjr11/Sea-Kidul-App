/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.ui.HomeActivity
import com.muramsyah.seakidul.ui.onboarding.OnBoardingActivity
import java.util.*

object ActivityHelper {
    fun showNotice(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setMessage("Untuk menjalankan aplikasi ini kmau perlu mengijinkan permission lokasi kamu!")
            .setTitle("Pemberitahuan")
            .setPositiveButton("Oke") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun checkPermission(permission: String, appCompatActivity: AppCompatActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            appCompatActivity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun setLanguage(languageCode: String, resources: Resources, context: Context) {
        val locale = Locale(languageCode)
        val dm = resources.displayMetrics
        val conf = resources.configuration
        conf.locale = locale
        resources.updateConfiguration(conf, dm)

        val refreshIntent = Intent(context, OnBoardingActivity::class.java)
        context.startActivity(refreshIntent)
    }

    fun setUIMode(isDarkMode: Boolean) {
        if (isDarkMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}