package fragment

import adapter.HomeRecyclerAdapter
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pratishtha.foodrunner.R
import model.Restaurant
import util.ConnectionManager


class HomeFragment : Fragment() {

    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager

    lateinit var btnCheckInternet:Button

    val restaurantInfoList = arrayListOf<Restaurant>(


        Restaurant("ABC Food Court", "4.1", "299", R.drawable.r2),
        Restaurant("XYZ Cafe", "3.4", "399", R.drawable.r9),
        Restaurant("BurgerVala", "4.8", "379", R.drawable.r6),
        Restaurant("MNO Restaurant", "4.5", "249", R.drawable.r3),
        Restaurant("Verma's CakeOClock", "2.4", "149", R.drawable.r4),
        Restaurant("LMN Restaurant", "3.1", "139", R.drawable.r7),
        Restaurant("DEF Food Court", "4.0", "349", R.drawable.r5),
        Restaurant("PQR Restaurant", "3.7", "299", R.drawable.r1),
        Restaurant("abc Cafe", "3.9", "399", R.drawable.r8),
        Restaurant("Coffee House", "4.3", "249", R.drawable.r9)

    )

    lateinit var recyclerAdapter: HomeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_home, container, false )

        recyclerView=view.findViewById(R.id.recyclerView)
        layoutManager=LinearLayoutManager(activity)

        recyclerAdapter= HomeRecyclerAdapter(activity as Context,restaurantInfoList)
        recyclerView.adapter=recyclerAdapter
        recyclerView.layoutManager=layoutManager

        btnCheckInternet=view.findViewById(R.id.btnCheckInternet)
        btnCheckInternet.setOnClickListener{

            if(ConnectionManager().checkConnectivity(activity as Context)){

                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok"){text ,listener->

                }
                dialog.setNegativeButton("Cancel"){text ,listener->

                }
                dialog.create()
                dialog.show()
            }
            else{
                val dialog=AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Connect to Internet")
                dialog.setPositiveButton("Ok"){text ,listener->

                }
                dialog.setNegativeButton("Cancel"){text ,listener->

                }
                dialog.create()
                dialog.show()
            }
        }

        return view
    }

}
