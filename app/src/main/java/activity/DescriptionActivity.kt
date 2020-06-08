package activity

import adapter.DescriptionRecyclerAdapter
import adapter.HomeRecyclerAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pratishtha.foodrunner.R
import kotlinx.android.synthetic.main.activity_description.*
import model.Description
import model.Restaurant
import org.json.JSONObject
import util.ConnectionManager
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager

    val dishInfoList= arrayListOf<Description>()
    lateinit var recyclerAdapter: DescriptionRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var restaurantId:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        recyclerView=findViewById(R.id.recyclerView)

        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE

        layoutManager=LinearLayoutManager(this@DescriptionActivity)


        if (intent != null) {
            restaurantId = intent.getStringExtra("restaurant_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected Error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (restaurantId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected Error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue=Volley.newRequestQueue(this@DescriptionActivity)


        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)){
        val jsonRequest=object :JsonObjectRequest(Method.GET,
            "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId",null,Response.Listener {

            try {
                val obj2 = it.getJSONObject("data")
                val success = obj2.getBoolean("success")
                if (success) {
                    val data = obj2.getJSONArray("data")
                    progressLayout.visibility = View.GONE
                    for (i in 0 until data.length()) {
                        val dishJsonObject = data.getJSONObject(i)
                        val dishObject = Description(

                            dishJsonObject.getString("id"),
                            dishJsonObject.getString("name"),
                            dishJsonObject.getString("cost_for_one")
                        )

                        dishInfoList.add(dishObject)
                        recyclerAdapter = DescriptionRecyclerAdapter(this@DescriptionActivity, dishInfoList)

                        recyclerView.adapter = recyclerAdapter
                        recyclerView.layoutManager = layoutManager
                    }
                    }else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }catch(e:Exception)
            {
                Toast.makeText(this@DescriptionActivity,"Some Exception Occurred $e",Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
            Toast.makeText(this@DescriptionActivity,"Volley error",Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "05ed6a9a010009"
                return headers
            }
        }
            queue.add(jsonRequest)
        }
        else{
    val dialog= AlertDialog.Builder(this@DescriptionActivity)
    dialog.setTitle("Error")
    dialog.setMessage("Internet Connection not Found")
    dialog.setPositiveButton("Open Settings"){ text,listener->
        val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(settingsIntent)
        finish()

    }
    dialog.setNegativeButton("Exit"){ text,listener->

        ActivityCompat.finishAffinity(this@DescriptionActivity)
    }
    dialog.create()
    dialog.show()
       }
    }
}
