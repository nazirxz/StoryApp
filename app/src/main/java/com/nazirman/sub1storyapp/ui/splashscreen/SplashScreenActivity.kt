package com.nazirman.sub1storyapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.ui.auth.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        var handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity,
                MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}
