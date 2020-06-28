package activity

import adapter.CartRecyclerAdapter
import adapter.DescriptionRecyclerAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
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
import database.OrderEntity
import database.RestaurantDatabase
import kotlinx.android.synthetic.main.activity_cart.*
import model.Description
import org.json.JSONArray
import org.json.JSONObject
import util.ConnectionManager
import util.SessionManager

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    val orderList=ArrayList<Description>()
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var rlMyCart:RelativeLayout
    lateinit var txtResName:TextView
    private lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var frameLayout: FrameLayout
    lateinit var btnOrder: Button
    lateinit var resId:String
    lateinit var resName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var sessionManager = SessionManager(this@CartActivity)
        var sharedPreferences = getSharedPreferences(sessionManager.PREF_NAME, Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this@CartActivity)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        txtResName = findViewById(R.id.txtResName)

        progressBar = findViewById(R.id.progressBar)
        rlMyCart = findViewById(R.id.rlMyCart)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE
        btnOrder=findViewById(R.id.btnOrder)
        btnOrder.visibility = View.VISIBLE

        setUpToolbar()

        if (intent != null) {
            resId = intent.getStringExtra("resId")
            resName = intent.getStringExtra("resName")as String
            txtResName.text=resName
        } else {
            finish()
            Toast.makeText(
                this@CartActivity,
                "Some unexpected Error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (resId == "0") {
            finish()
            Toast.makeText(
                this@CartActivity,
                "Some unexpected Error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        cartList()
        placeOrder()
    }


    class GetItemsDBAsync(context:Context): AsyncTask<Void, Void, List<OrderEntity>>()
    {
        val db= Room.databaseBuilder( context, RestaurantDatabase::class.java,"restaurants-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderDao().getAllOrders()
        }
    }

    private  fun cartList() {

        val list=GetItemsDBAsync(applicationContext).execute().get()
        for(element in list){

            orderList.addAll(Gson().fromJson(element.foodItems,Array<Description>::class.java).asList())
        }
        if(orderList.isEmpty())
        {
            rlMyCart.visibility=View.GONE
            progressLayout.visibility=View.VISIBLE
        }
        else{
            rlMyCart.visibility=View.VISIBLE
            progressLayout.visibility=View.GONE
        }

        recyclerAdapter= CartRecyclerAdapter(orderList,this@CartActivity)
        layoutManager = LinearLayoutManager(this@CartActivity)
        recyclerView.layoutManager=layoutManager
        recyclerView.itemAnimator=DefaultItemAnimator()
        recyclerView.adapter=recyclerAdapter
    }

    private fun placeOrder() {
        var sum = 0
        for (i in 0 until orderList.size) {
            sum += orderList[i].dishPrice.toInt()
        }
        val total = "Place Order(Total: Rs. $sum)"
        btnOrder.text = total
        btnOrder.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            sendRequest()
        }
    }

    private  fun setUpToolbar() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private  fun sendRequest() {

        val queue = Volley.newRequestQueue(this@CartActivity)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        if (ConnectionManager().checkConnectivity(this)) {
            try {
                progressLayout.visibility = View.GONE

                val jsonParams = JSONObject()
                jsonParams.put(
                    "user_id",
                    this@CartActivity.getSharedPreferences(
                        getString(R.string.preference_file_name),
                        Context.MODE_PRIVATE
                    ).getString(
                        "user_id", "0"
                    ) as String
                )

                jsonParams.put("restaurant_id", resId?.toString() as String)
                var total = 0
                for (i in 0 until orderList.size) {
                    total += orderList[i].dishPrice.toInt()
                }
                jsonParams.put("total_cost", total.toString())
                val dishArray = JSONArray()
                for (i in 0 until orderList.size) {
                    val dish_id = JSONObject()
                    dish_id.put("food_item_id", orderList[i].dishId)
                    dishArray.put(i, dish_id)
                }
                jsonParams.put("food", dishArray)
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        val obj = it.getJSONObject("data")
                        val success = obj.getBoolean("success")
                        if (success) {
                                ClearDBAsync(applicationContext, resId.toString()).execute().get()
                            DescriptionRecyclerAdapter.isCartEmpty = true
                            Toast.makeText(this@CartActivity,"Order Placed",Toast.LENGTH_SHORT).show()
                            val intent= Intent(this,PlaceOrderActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            rlMyCart.visibility = View.VISIBLE
                            Toast.makeText(
                                this@CartActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }, Response.ErrorListener {
                        rlMyCart.visibility = View.VISIBLE
                        Toast.makeText(
                            this@CartActivity,
                            "Volley Error Occurred",
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
            } catch (e: Exception) {
                rlMyCart.visibility = View.VISIBLE
                e.printStackTrace()
            }
        } else {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                finishAffinity()//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()
        }
    }

    class ClearDBAsync(context: Context,val resId:String):AsyncTask<Void,Void,Boolean>(){
        val db=Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().deleteOrders(resId)
            db.close()
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onBackPressed() {
        val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        alterDialog.setTitle("Alert!")
        alterDialog.setMessage("Going back will remove everything from cart")
        alterDialog.setPositiveButton("Okay") { text, listener ->
            ClearDBAsync(applicationContext, resId.toString()).execute().get()
            DescriptionRecyclerAdapter.isCartEmpty=true
            super.onBackPressed()
        }
        alterDialog.setNegativeButton("No") { text, listener ->

        }
        alterDialog.show()
    }

    override fun onStop() {
        ClearDBAsync(applicationContext, resId.toString()).execute().get()
        DescriptionRecyclerAdapter.isCartEmpty=true
        super.onStop()
    }
}














