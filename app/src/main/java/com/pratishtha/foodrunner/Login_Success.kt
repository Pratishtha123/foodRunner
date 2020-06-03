package com.pratishtha.foodrunner

import activity.Main2_Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Login_Success : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login__success)

        Handler().postDelayed({
            val startAct=Intent(this@Login_Success, Main2_Activity::class.java)
            startActivity(startAct)
        },2000)
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
