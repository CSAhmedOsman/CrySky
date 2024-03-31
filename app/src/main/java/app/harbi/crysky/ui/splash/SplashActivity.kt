package app.harbi.crysky.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.harbi.crysky.databinding.ActivitySplashBinding
import app.harbi.crysky.ui.home.view.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            intent.setAction(Intent.ACTION_VIEW)
            startActivity(intent)
            finish()
        }
    }

}
