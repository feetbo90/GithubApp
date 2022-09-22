package com.my.githubapp.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.my.githubapp.R
import com.my.githubapp.ui.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep((3 * 1000).toLong())
                    val i = Intent(baseContext, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } catch (e: Exception) {
                }
            }
        }
        background.start()
    }
}