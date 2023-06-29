package com.example.eshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.eshop.LoginSignUp.LoginScreen

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashOut: View = findViewById(R.id.splash_outLet)
        val logo: View = findViewById(R.id.logo)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)


        splashOut.setScaleY(10F)
        splashOut.setScaleX(10F)

        logo.setScaleY(0.2f)
        logo.setScaleX(0.2f)


        splashOut.animate().scaleX(0.01f).scaleY(0.01f).setDuration(1000)
        logo.animate().scaleY(1.2f).scaleX(1.2f).setDuration(800).setStartDelay(1000)

        android.os.Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            finish()
        }, 3000)

    }
}