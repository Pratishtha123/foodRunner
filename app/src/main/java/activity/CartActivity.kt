package activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pratishtha.foodrunner.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var progressLayout:RelativeLayout
    lateinit var rlCart:RelativeLayout
    lateinit var txtResName:TextView
    lateinit var frameLayout: FrameLayout
    lateinit var btnPlaceOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this@CartActivity)
        coordinateLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        txtResName=findViewById(R.id.txtResName)
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility= View.GONE
        btnPlaceOrder.visibility= View.VISIBLE



        btnPlaceOrder.setOnClickListener {
            val intent= Intent(this@CartActivity,PlaceOrderActivity::class.java)
            startActivity(intent)
        }
    }
}
