package com.kl3jvi.takeawaytask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kl3jvi.common.util.launchActivity
import com.kl3jvi.feature_home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchActivity<HomeActivity> { finish() }
    }
}
