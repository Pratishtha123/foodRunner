package com.pratishtha.foodrunner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register_Activity : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etMobileNumber2:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword2:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btnRegisterPage:Button

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var mobilePattern = "[0-9]{10}"

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

            if(etName.text.toString().isEmpty())
                Toast.makeText(this@Register_Activity,"Enter Name",Toast.LENGTH_LONG).show()
            else if(etEmail.text.toString().isEmpty())
                Toast.makeText(this@Register_Activity,"Enter Email Id",Toast.LENGTH_LONG).show()
            else if(etMobileNumber2.text.toString().isEmpty())
                Toast.makeText(this@Register_Activity,"Enter Mobile Number",Toast.LENGTH_LONG).show()
            else if(etAddress.text.toString().isEmpty())
                Toast.makeText(this@Register_Activity,"Enter Delivery Address",Toast.LENGTH_LONG).show()
            else if(etPassword2.text.toString().isEmpty())
                Toast.makeText(this@Register_Activity,"Enter Password",Toast.LENGTH_LONG).show()
            else if(etPassword2.text.toString()!=etConfirmPassword.text.toString())
                Toast.makeText(this@Register_Activity,"Passwords doesn't match. Please try again!",Toast.LENGTH_LONG).show()
            else if(!etEmail.text.toString().trim().matches(emailPattern.toRegex()))
                Toast.makeText(this@Register_Activity,"Enter a valid Email Id",Toast.LENGTH_LONG).show()
            else if(!etMobileNumber2.text.toString().trim().matches(mobilePattern.toRegex()))
                Toast.makeText(this@Register_Activity,"Enter a valid Mobile number",Toast.LENGTH_LONG).show()


            else {
                Toast.makeText(this@Register_Activity, "Successfully Registered", Toast.LENGTH_LONG)
                    .show()

                val intent = Intent(this@Register_Activity, Show_Details::class.java)
                intent.putExtra("Name", etName.text.toString())
                intent.putExtra("Email", etEmail.text.toString())
                intent.putExtra("Mobile", etMobileNumber2.text.toString())
                intent.putExtra("Address", etAddress.text.toString())

                startActivity(intent)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}