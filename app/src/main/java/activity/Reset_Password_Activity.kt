package activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pratishtha.foodrunner.R
import org.json.JSONObject
import util.ConnectionManager
import java.lang.Exception

class Reset_Password_Activity : AppCompatActivity() {

    private lateinit var txtEnter: TextView
    private lateinit var etOTP: EditText
    private lateinit var etPassword3: EditText
    private lateinit var etConfirmPassword3: EditText
    private lateinit var btnSubmit: Button
    private lateinit var rlContent3: RelativeLayout
    private lateinit var mobileNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        txtEnter = findViewById(R.id.txtEnter)
        etOTP = findViewById(R.id.etOTP)
        etPassword3 = findViewById(R.id.etPassword3)
        etConfirmPassword3 = findViewById(R.id.etConfirmPassword3)
        btnSubmit = findViewById(R.id.btnSubmit)
        rlContent3 = findViewById(R.id.rlContent3)

        rlContent3.visibility = View.VISIBLE
        if (intent != null) {
            mobileNumber = intent.getStringExtra("user_mobile") as String
        }
        btnSubmit.setOnClickListener {
            rlContent3.visibility = View.GONE
            if (ConnectionManager().checkConnectivity(this@Reset_Password_Activity)) {
                if (etOTP.text.length == 4) {
                    if (etPassword3.length()>=4) {
                        if (etPassword3.text.toString()==etConfirmPassword3.text.toString())
                         {
                            resetPassword(
                                mobileNumber,
                                etOTP.text.toString(),
                                etPassword3.text.toString()
                            )
                        } else {
                            rlContent3.visibility = View.VISIBLE
                            Toast.makeText(
                                this@Reset_Password_Activity,
                                "Passwords do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        rlContent3.visibility = View.VISIBLE
                        Toast.makeText(
                            this@Reset_Password_Activity,
                            "Invalid Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    rlContent3.visibility = View.VISIBLE
                    Toast.makeText(
                        this@Reset_Password_Activity,
                        "Incorrect OTP",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val dialog = AlertDialog.Builder(this@Reset_Password_Activity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()

                }
                dialog.setNegativeButton("Exit") { text, listener ->

                    ActivityCompat.finishAffinity(this@Reset_Password_Activity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    private fun resetPassword(mobileNumber: String, otp: String, password: String) {
        val queue = Volley.newRequestQueue(this@Reset_Password_Activity)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number", mobileNumber)
        jsonParams.put("password", password)
        jsonParams.put("otp", otp)

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                try {
                    val obj = it.getJSONObject("data")
                    val success = obj.getBoolean("success")
                    if (success) {
                        val builder = AlertDialog.Builder(this@Reset_Password_Activity)
                        builder.setTitle("Confirmation")
                        builder.setMessage("Your Password has been successfully changed")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Ok") { _, _ ->
                            startActivity(
                                Intent(
                                    this@Reset_Password_Activity,
                                    Main_Activity::class.java
                                )
                            )
                            ActivityCompat.finishAffinity(this@Reset_Password_Activity)
                        }
                        builder.create().show()
                    } else {
                        rlContent3.visibility = View.VISIBLE
                        Toast.makeText(
                            this@Reset_Password_Activity,
                            "Some error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    rlContent3.visibility = View.VISIBLE
                    Toast.makeText(
                        this@Reset_Password_Activity,
                        "Some exception occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                rlContent3.visibility = View.VISIBLE
                Toast.makeText(
                    this@Reset_Password_Activity,
                    "Volley error occurred!",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
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
