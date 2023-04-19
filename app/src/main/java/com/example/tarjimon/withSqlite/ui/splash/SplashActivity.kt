package com.example.tarjimon.withSqlite.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.tarjimon.R
import com.example.tarjimon.databinding.ActivitySplashBinding
import com.example.tarjimon.withSqlite.db.SharedPref
import com.example.tarjimon.withSqlite.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {


    private lateinit var binding: ActivitySplashBinding
    private val shardpr by lazy { SharedPref.getInstens(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val time = 1500L
        Handler(Looper.getMainLooper()).postDelayed({
            if (shardpr.language == "uzbek") {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("lang", "uzbek")
                startActivity(intent)

            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("lang", "english")
                startActivity(intent)
            }
        }, 1500L)


    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}