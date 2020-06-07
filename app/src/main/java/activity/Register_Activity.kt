package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Request.*
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.pratishtha.foodrunner.R
import fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import util.SessionManager
import java.lang.Exception

class Register_Activity : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etMobileNumber2:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword2:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btnRegisterPage:Button

    lateinit var sharedpreferences: SharedPreferences
    lateinit var sessionManager:SessionManager

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var mobilePattern = "[0-9]{10}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title="Register Yourself"

        sharedpreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

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
            else if (etPassword2.length()<4) {
                Toast.makeText(this@Register_Activity, "Weak Password", Toast.LENGTH_LONG).show()
            }

            else {
                sendRegisterRequest(etName.text.toString(),etMobileNumber2.text.toString(),etAddress.text.toString(),etPassword2.text.toString(),etEmail.text.toString())
               /* val intent = Intent(this@Register_Activity, Show_Details::class.java)
                intent.putExtra("Name", etName.text.toString())
                intent.putExtra("Email", etEmail.text.toString())
                intent.putExtra("Mobile", etMobileNumber2.text.toString())
                intent.putExtra("Address", etAddress.text.toString())

                startActivity(intent)*/
                Toast.makeText(this@Register_Activity, "Successfully Registered", Toast.LENGTH_LONG)
                    .show()
                val intent=Intent(this@Register_Activity,Main2_Activity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    private fun sendRegisterRequest(name:String,phone:String,address:String,password:String,email:String)
    {

        val url="http://13.235.250.119/v2/register/fetch_result"
        val queue=Volley.newRequestQueue(this@Register_Activity)
        val jsonParams=JSONObject()
        jsonParams.put("name",name)
        jsonParams.put("mobile_number",phone)
        jsonParams.put("password",password)
        jsonParams.put("address",address)
        jsonParams.put("email",email)

        val jsonObjectRequest=object :JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonParams,
            Response.Listener {

                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val response = data.getJSONObject("data")
                        sharedpreferences.edit().putString("user_id", response.getString("user_id"))
                            .apply()
                        sharedpreferences.edit().putString("user_name", response.getString("name"))
                            .apply()
                        sharedpreferences.edit()
                            .putString("user_mobile_number", response.getString("mobile_number"))
                            .apply()
                        sharedpreferences.edit()
                            .putString("user_address", response.getString("address")).apply()
                        sharedpreferences.edit()
                            .putString("user_email", response.getString("email")).apply()

                        sessionManager.setLogin(true)
                        startActivity(
                            Intent(this@Register_Activity, Main2_Activity::class.java)
                        )
                        finish ()
                }else{
                        rlRegister.visibility= View.VISIBLE
                        //progressBar.visibility=View.INVISIBLE
                        val errorMessage=data.getString("errorMessage")
                        Toast.makeText(this@Register_Activity,errorMessage,Toast.LENGTH_SHORT).show()
                    }

                }catch(e:Exception){
                    rlRegister.visibility=View.VISIBLE
                    //progressBar.visibility=View.INVISIBLE
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                    Toast.makeText(this@Register_Activity,it.message,Toast.LENGTH_SHORT).show()
                     rlRegister.visibility=View.VISIBLE
                     //progressBar.visibility=View.VISIBLE
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "05ed6a9a010009"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }
}