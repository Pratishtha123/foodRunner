package activity

import adapter.DescriptionRecyclerAdapter
import adapter.GetFavAsyncTask
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
import androidx.room.RoomDatabase
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pratishtha.foodrunner.R
import database.OrderEntity
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

    var restaurantId: String = "100"


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
            proceedToCart()
        }


        layoutManager = LinearLayoutManager(this@DescriptionActivity)


        if (intent != null) {
            restaurantId = intent.getStringExtra("restaurant_id")
            restaurantName = intent.getStringExtra("restaurant_name")as String

            val listOfFav = GetFavAsyncTask(this).execute().get()

            if (listOfFav.isNotEmpty() && listOfFav.contains(restaurantId)) {

                imgFav.setBackgroundResource(R.drawable.fav)
            } else {
                imgFav.setBackgroundResource(R.drawable.fav2)
            }
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
                        orderList.clear()
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

    private fun proceedToCart(){
        val gson=Gson()
        val foodItems=gson.toJson(orderList)
        val async = CartItems(this@DescriptionActivity, restaurantId.toString(), foodItems, 1).execute()
        val result = async.get()
        if (result) {

            val intent=Intent(this@DescriptionActivity,CartActivity::class.java)
            intent.putExtra("resId", restaurantId )
            intent.putExtra("resName", restaurantName)
            startActivity(intent)

        } else {
            Toast.makeText(this@DescriptionActivity, "Some unexpected error", Toast.LENGTH_SHORT).show()
        }

    }

    class CartItems(context: Context, private val restaurantId:String, private val foodItems:String, val mode:Int):
        AsyncTask<Void,Void,Boolean>(){
        val db=Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    db.orderDao().insertOrder(OrderEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }

                2 -> {
                    db.orderDao().deleteOrder(OrderEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }
            }
            return false
        }
    }

    override fun onBackPressed() {


        if(orderList.size > 0) {


            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("Alert!")
            alterDialog.setMessage("Going back will remove everything from cart")
            alterDialog.setPositiveButton("Okay") { text, listener ->
                super.onBackPressed()
            }
            alterDialog.setNegativeButton("No") { text, listener ->

            }
            alterDialog.show()
        }else{
            super.onBackPressed()
        }

    }

   private fun setUpToolbar(name:String){
       toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title=name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
