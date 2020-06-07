package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pratishtha.foodrunner.R

class Splash_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent=Intent(this@Splash_Activity, Main_Activity::class.java)
            startActivity(intent)
        },1000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
