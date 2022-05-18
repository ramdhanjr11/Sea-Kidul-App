/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
}