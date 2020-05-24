package com.pratishtha.foodrunner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

    var mobilePattern = "[0-9]{10}"

    lateinit var sharedpreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        val isLoggedIn=sharedpreferences.getBoolean("isLoggedIn",false)

        setContentView(R.layout.activity_main)

        if(isLoggedIn)
        {
            val intent=Intent(this@Main_Activity,Login_Success::class.java)
            startActivity(intent)
            finish()
        }




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

        btnLogin.setOnClickListener{

            var mobilenumber = etMobileNumber.text.toString()
            var password = etPassword.text.toString()

            if(etMobileNumber.text.toString().isEmpty())
                Toast.makeText(this@Main_Activity,"Enter Mobile Number",Toast.LENGTH_LONG).show()
            else if(etPassword.text.toString().isEmpty())
                Toast.makeText(this@Main_Activity,"Enter Password",Toast.LENGTH_LONG).show()
            else if(!etMobileNumber.text.toString().trim().matches(mobilePattern.toRegex()))
                Toast.makeText(this@Main_Activity,"Enter a valid Mobile number",Toast.LENGTH_LONG).show()

            else if((mobilenumber==validmob)&&(password==validpass))
            {
                val intent=Intent(this@Main_Activity,Login_Success::class.java)
                sharedpreferences.edit().putBoolean("isLoggedIn",true).apply()

                startActivity(intent)
            }

            else
            {
                Toast.makeText(this@Main_Activity,"Incorrect Credentials",Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onPause() {
        super.onPause()

        var Login=sharedpreferences.getBoolean("isLoggedIn",false)
         if(Login)
             finish()

    }

}

