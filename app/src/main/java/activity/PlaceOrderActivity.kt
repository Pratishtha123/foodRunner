package activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pratishtha.foodrunner.R

lateinit var btnOk:Button

class PlaceOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_placed_dialog)

        btnOk=findViewById(R.id.btnOk)

        btnOk.setOnClickListener{
            val intent= Intent(this@PlaceOrderActivity,Main2_Activity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        //forcing user to press Ok.
    }
}
