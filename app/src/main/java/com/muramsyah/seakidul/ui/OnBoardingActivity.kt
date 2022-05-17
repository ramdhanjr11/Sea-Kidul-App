/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muramsyah.seakidul.R

class OnBoardingActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Slide 1
        addSlide(AppIntroFragment.createInstance(
            title = "Melihat Lokasi Evakuasi",
            description = "Kamu dapat melihat lokasi evakuasi yang aman disekitaran kamu!",
            imageDrawable = R.drawable.onboarding1,
            titleColorRes = R.color.white_200,
            descriptionColorRes = R.color.green_200,
            backgroundColorRes = R.color.green_400
        ))

        // Slide 2
        addSlide(AppIntroFragment.createInstance(
            title = "Rute Evakuasi Terdekat!",
            description = "Dapatkan rute evakuasi terdekat saat terjadi bencana tsunami disekitaran kamu!",
            imageDrawable = R.drawable.onboarding2,
            titleColorRes = R.color.white_200,
            descriptionColorRes = R.color.green_200,
            backgroundColorRes = R.color.green_400
        ))

        // Slide 3
        addSlide(AppIntroFragment.createInstance(
            title = "Alarm Warning System!",
            description = "Memberitahukan kamu ketika terjadi bencana Tsunami disekitar kamu!",
            imageDrawable = R.drawable.onboarding3,
            titleColorRes = R.color.white_200,
            descriptionColorRes = R.color.green_200,
            backgroundColorRes = R.color.green_400
        ))

        setTransformer(AppIntroPageTransformerType.Fade)

        askForPermissions(
            permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            slideNumber = 2,
            required = true)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToNextSlide()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, HomeActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }

    override fun onUserDeniedPermission(permissionName: String) {
        super.onUserDeniedPermission(permissionName)
        showNotice()
    }

    override fun onUserDisabledPermission(permissionName: String) {
        super.onUserDisabledPermission(permissionName)
        showNotice()
    }

    private fun showNotice() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Untuk menjalankan aplikasi ini kmau perlu mengijinkan permission lokasi kamu!")
            .setTitle("Pemberitahuan")
            .setPositiveButton("Oke") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}