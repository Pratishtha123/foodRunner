package com.pratishtha.foodrunner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Register_Activity : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etMobileNumber2:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword2:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btnRegisterPage:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title="Register Yourself"

        etName=findViewById(R.id.etName)
        etEmail=findViewById(R.id.etEmail)
        etMobileNumber2=findViewById(R.id.etMobileNumber2)
        etAddress=findViewById(R.id.etAddress)
        etPassword2=findViewById(R.id.etPassword2)
        etConfirmPassword=findViewById(R.id.etConfirmPassword)
        btnRegisterPage=findViewById(R.id.btnRegisterPage)

        btnRegisterPage.setOnClickListener {

            Toast.makeText(this@Register_Activity,"Successfully Registered",Toast.LENGTH_LONG).show()

            val intent=Intent(this@Register_Activity,Show_Details::class.java)
            intent.putExtra("Name",etName.text.toString())
            intent.putExtra("Email",etEmail.text.toString())
            intent.putExtra("Mobile",etMobileNumber2.text.toString())
            intent.putExtra("Address",etAddress.text.toString())

            startActivity(intent)

        }

    }
}