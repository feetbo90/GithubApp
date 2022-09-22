package com.my.githubapp.ui.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.my.githubapp.R
import com.my.githubapp.databinding.ActivitySettingBinding
import com.my.githubapp.ui.viewmodel.settings.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(getString(R.string.settings))

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    settingViewModel.getThemeSetting.collect { state ->
                        Log.d("Test value ", state.toString())
                        if (state) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        binding.switchDark.isChecked = state
                    }
                }
            }
        }

        binding.switchDark.setOnCheckedChangeListener { _, isChecked ->
            binding.switchDark.isChecked = isChecked
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbarSetting)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}