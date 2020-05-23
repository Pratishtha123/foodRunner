package com.pratishtha.foodrunner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class Forgot_Activity : AppCompatActivity() {


    lateinit var txtInstruction:TextView
    lateinit var etMobileNumber3:EditText
    lateinit var etMailAddress:EditText
    lateinit var btnNext:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_)
        title="Retrieve Password"

        txtInstruction=findViewById(R.id.txtInstruction)
        etMobileNumber3=findViewById(R.id.etMobileNumber3)
        etMailAddress=findViewById(R.id.etMailAddress)
        btnNext=findViewById(R.id.btnNext)

        btnNext.setOnClickListener{

            Toast.makeText(this@Forgot_Activity,"Refer to your Mail id to change Password",Toast.LENGTH_LONG).show()
        }



    }
}
