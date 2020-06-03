package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.pratishtha.foodrunner.R

class Show_Details : AppCompatActivity() {


    lateinit var txtName:TextView
    lateinit var txtEmail_add:TextView
    lateinit var txtMobileNumber4:TextView
    lateinit var txtAddress1:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__details)
        title="Registered"

        txtName=findViewById(R.id.txtName)
        txtEmail_add=findViewById(R.id.txtEmail_add)
        txtMobileNumber4=findViewById(R.id.txtMobileNumber4)
        txtAddress1=findViewById(R.id.txtAddress1)

        if(intent!=null)
        {
            var name=intent.getStringExtra("Name")
            var mail=intent.getStringExtra("Email")
            var mob=intent.getStringExtra("Mobile")
            var add=intent.getStringExtra("Address")

            txtName.setText("Name: $name")
            txtEmail_add.setText("Email: $mail")
            txtMobileNumber4.setText("Mobile: $mob")
            txtAddress1.setText("Address: $add")

        }

    }
}
