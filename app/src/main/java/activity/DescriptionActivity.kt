package activity

import adapter.DescriptionRecyclerAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pratishtha.foodrunner.R
import database.RestaurantDatabase
import model.Description
import util.ConnectionManager
import util.SessionManager
import kotlin.collections.HashMap

class DescriptionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var imgFav: ImageButton
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var btnGoToCart: Button

    private val dishInfoList = arrayListOf<Description>()
    private val orderList = arrayListOf<Description>()

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerAdapter: DescriptionRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var restaurantName: String
    lateinit var sessionManager: SessionManager

    var restaurantId: String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        sessionManager = SessionManager(this@DescriptionActivity)
        sharedPreferences = getSharedPreferences(sessionManager.PREF_NAME, Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerView)

        //setHasOptionsMenu(true)

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        btnGoToCart = findViewById(R.id.btnGoToCart)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        imgFav = findViewById(R.id.imgFav)

        btnGoToCart.setOnClickListener {
            Toast.makeText(this@DescriptionActivity,"cart under construcution",Toast.LENGTH_SHORT).show()
        }


        layoutManager = LinearLayoutManager(this@DescriptionActivity)


        if (intent != null) {
            restaurantId = intent.getStringExtra("restaurant_id")
            restaurantName = intent.getStringExtra("restaurant_name")as String
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

        setUpToolbar(restaurantName)

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(Method.GET,
                    "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId", null, Response.Listener {

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
                            recyclerAdapter = DescriptionRecyclerAdapter(this@DescriptionActivity, dishInfoList,
                            object:DescriptionRecyclerAdapter.OnItemClickListener{
                                override fun onAddItemClick(dishObject:Description) {
                                    orderList.add(dishObject)
                                    if (orderList.size > 0) {
                                        btnGoToCart.visibility = View.VISIBLE
                                        DescriptionRecyclerAdapter.isCartEmpty = false
                                    }
                                }

                                override fun onRemoveItemClick(dishObject:Description) {
                                    orderList.remove(dishObject)
                                    if (orderList.isEmpty()) {
                                        btnGoToCart.visibility = View.GONE
                                        DescriptionRecyclerAdapter.isCartEmpty = true
                                    }
                                }

                            })

                            recyclerView.adapter = recyclerAdapter
                            recyclerView.itemAnimator = DefaultItemAnimator()
                            recyclerView.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@DescriptionActivity, "Some Exception Occurred $e", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley error", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "05ed6a9a010009"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit") { text, listener ->

                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    fun setUpToolbar(name:String){
        setSupportActionBar(toolbar)
        supportActionBar?.title=name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    Main2_Activity::class.java
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
