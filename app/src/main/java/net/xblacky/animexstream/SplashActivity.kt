package net.xblacky.animexstream

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}