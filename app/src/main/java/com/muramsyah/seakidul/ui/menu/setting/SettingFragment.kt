/*
 * Copyright (c) 2022.
 * Created By M Ramdhan Syahputra on 16-05-2022
 * Github : https://github.com/ramdhanjr11
 * LinkedIn : https://www.linkedin.com/in/ramdhanjr11
 * Instagram : ramdhan.official
 */

package com.muramsyah.seakidul.ui.menu.setting

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.muramsyah.seakidul.databinding.FragmentSettingBinding
import com.muramsyah.seakidul.ui.HomeActivity
import com.muramsyah.seakidul.ui.onboarding.OnBoardingActivity
import com.muramsyah.seakidul.utils.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvSwitchToEnglish.setOnClickListener(this)
        binding.cvSwitchToDark.setOnClickListener(this)
        binding.switchToEnglish.isClickable = false
        binding.switchToDark.isClickable = false
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isDarkMode.observe(viewLifecycleOwner) {
            binding.switchToDark.isChecked = it
            if (it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        viewModel.isEnglish.observe(viewLifecycleOwner) {
            binding.switchToEnglish.isChecked = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view) {
                binding.cvSwitchToEnglish -> {
                    viewModel.setEnglishLanguage()
                    changeLanguage()
                    val intent = Intent(context, OnBoardingActivity::class.java)
                    startActivity(intent)
                    requireActivity().finishAffinity()
                }
                binding.cvSwitchToDark -> {
                    viewModel.setDarkMode()
                }
            }
        }
    }

    private fun changeLanguage() {
        viewModel.isEnglish.observe(viewLifecycleOwner) {
            if (it) ActivityHelper.setLanguage("EN", resources, requireContext())
            else ActivityHelper.setLanguage("ID", resources, requireContext())
        }
    }
}