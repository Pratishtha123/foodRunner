package com.pratishtha.foodrunner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Main_Activity : AppCompatActivity() {

    lateinit var txtForgot : TextView
    lateinit var btnLogin : Button
    lateinit var etPassword : EditText
    lateinit var etMobileNumber : EditText
    lateinit var txtRegister : TextView

    val validmob ="1111111111"
    val validpass="11111"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtForgot=findViewById(R.id.txtForgot)
        btnLogin=findViewById(R.id.btnLogin)
        etPassword=findViewById(R.id.etPassword)
        etMobileNumber=findViewById(R.id.etMobileNumber)
        txtRegister=findViewById(R.id.txtRegister)

        txtRegister.setOnClickListener {

            val intent = Intent(this@Main_Activity, Register_Activity::class.java)
            startActivity(intent)

         }

        txtForgot.setOnClickListener{

            val intent=Intent(this@Main_Activity,Forgot_Activity::class.java)
            startActivity(intent)

        }
    }
}

