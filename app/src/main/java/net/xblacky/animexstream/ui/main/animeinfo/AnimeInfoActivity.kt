package net.xblacky.animexstream.ui.main.animeinfo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animeinfo.*
import net.xblacky.animexstream.R

class AnimeInfoActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animeinfo)
        getBundleData()
    }

    private fun getBundleData(){
        val categoryUrl = intent.extras?.getString("categoryUrl")
        categoryUrl?.let {
            (animeInfoFragment as AnimeInfoFragment).updateCategoryUrl(it)
        }
    }
}