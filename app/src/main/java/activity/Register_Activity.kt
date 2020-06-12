package activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pratishtha.foodrunner.R
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import util.ConnectionManager
import util.SessionManager

class Register_Activity : AppCompatActivity() {

    lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etMobileNumber2:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword2:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btnRegisterPage:Button

    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout

    lateinit var sharedpreferences: SharedPreferences
    lateinit var sessionManager:SessionManager

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var mobilePattern = "[7-9][0-9]{9}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title="Register Yourself"

        sessionManager = SessionManager(this@Register_Activity)
        sharedpreferences = getSharedPreferences(sessionManager.PREF_NAME, Context.MODE_PRIVATE)

        etName=findViewById(R.id.etName)
        etEmail=findViewById(R.id.etEmail)
        etMobileNumber2=findViewById(R.id.etMobileNumber2)
        etAddress=findViewById(R.id.etAddress)
        etPassword2=findViewById(R.id.etPassword2)
        etConfirmPassword=findViewById(R.id.etConfirmPassword)
        btnRegisterPage=findViewById(R.id.btnRegisterPage)

        coordinateLayout=findViewById(R.id.coordinateLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)

        setUpToolbar()

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
                sendRequest(etName.text.toString(),etMobileNumber2.text.toString(),etAddress.text.toString(),etPassword2.text.toString(),etEmail.text.toString())

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
    private fun sendRequest(name:String,phone:String,address:String,password:String,email:String)
    {

        val url="http://13.235.250.119/v2/register/fetch_result"
        val queue=Volley.newRequestQueue(this@Register_Activity)
        val jsonParams=JSONObject()
        jsonParams.put("name",name)
        jsonParams.put("mobile_number",phone)
        jsonParams.put("password",password)
        jsonParams.put("address",address)
        jsonParams.put("email",email)

        if (ConnectionManager().checkConnectivity(this@Register_Activity)){
        val jsonObjectRequest=object :JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener {

                try {
                    val obj = it.getJSONObject("data")
                    val success = obj.getBoolean("success")
                    if (success) {
                        val response = obj.getJSONObject("data")
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
                        Toast.makeText(this@Register_Activity,"Some error occurred!",Toast.LENGTH_SHORT).show()
                    }
                }catch(e:Exception){
                    rlRegister.visibility=View.VISIBLE
                    //progressBar.visibility=View.INVISIBLE
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                    Toast.makeText(this@Register_Activity,"Volley Error!",Toast.LENGTH_SHORT).show()
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
    }else
        {
            val dialog= AlertDialog.Builder(this@Register_Activity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Found")
            dialog.setPositiveButton("Open Settings"){ text,listener->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this.finish()

            }
            dialog.setNegativeButton("Exit"){ text,listener->

                ActivityCompat.finishAffinity(this@Register_Activity)
            }
            dialog.create()
            dialog.show()
        }
}
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    Main_Activity::class.java
                )
            )
        }
    }
}