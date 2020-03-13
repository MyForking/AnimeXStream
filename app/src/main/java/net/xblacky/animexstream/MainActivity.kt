package net.xblacky.animexstream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.xblacky.animexstream.ui.main.MainFragment
import net.xblacky.animexstream.ui.main.home.HomeFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commitNow()
        }
    }

}
