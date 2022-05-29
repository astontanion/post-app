package space.stanton.technicaltest.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import space.stanton.technicaltest.R
import space.stanton.technicaltest.databinding.ActivityMainBinding
import space.stanton.technicaltest.fragment.PostListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity, R.layout.activity_main)
    }
}

