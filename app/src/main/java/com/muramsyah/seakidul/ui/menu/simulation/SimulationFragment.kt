/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.menu.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.muramsyah.seakidul.databinding.FragmentSimulationBinding
import com.muramsyah.seakidul.service.DangerReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimulationFragment : Fragment() {

    private var _binding: FragmentSimulationBinding? = null
    private val binding get() = _binding!!
    private var onTriggered = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimulationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartSimulation.setOnClickListener {
            if (onTriggered) {
                Toast.makeText(context, "Tunggu sebentar!", Toast.LENGTH_SHORT).show()
            } else {
                onTriggered = true
                val dangerReceiver = DangerReceiver()
                dangerReceiver.triggeredPhone(requireContext())
                Toast.makeText(context, "Simulasi akan dimulai..", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onTriggered = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}