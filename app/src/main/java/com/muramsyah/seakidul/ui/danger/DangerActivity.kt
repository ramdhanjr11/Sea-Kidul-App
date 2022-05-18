/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.danger

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muramsyah.seakidul.R
import com.muramsyah.seakidul.databinding.ActivityDangerBinding
import com.muramsyah.seakidul.ui.route.RouteEvacuateActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class DangerActivity : AppCompatActivity() {

    private var _binding: ActivityDangerBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDangerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMediaPlayer()
        binding.btnGoEvacuate.setOnClickListener {
            mediaPlayer.stop()
            val intent = Intent(this, RouteEvacuateActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        val audioAttribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mediaPlayer.setAudioAttributes(audioAttribute)
        val afd = applicationContext.resources.openRawResourceFd(R.raw.tsunami_sirine)
        try {
            mediaPlayer.run { setDataSource(afd.fileDescriptor, afd.startOffset, afd.length) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}