package fragment

import adapter.HomeRecyclerAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pratishtha.foodrunner.R
import model.Restaurant


class HomeFragment : Fragment() {


    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager

    val restaurantInfoList = arrayListOf<Restaurant>(


        Restaurant("ABC Food Court", "4.1", "Rs. 299", R.drawable.r2),
        Restaurant("XYZ Cafe", "3.4", "Rs. 399", R.drawable.r9),
        Restaurant("BurgerVala", "4.8", "Rs. 379", R.drawable.r6),
        Restaurant("MNO Restaurant", "4.5", "Rs. 249", R.drawable.r3),
        Restaurant("Verma's CakeOClock", "2.4", "Rs. 149", R.drawable.r4),
        Restaurant("LMN Restaurant", "3.1", "Rs. 99", R.drawable.r7),
        Restaurant("DEF Restaurant", "4.0", "Rs. 349", R.drawable.r5),
        Restaurant("PQR Food Court", "3.7", "Rs. 299", R.drawable.r1),
        Restaurant("abc", "3.9", "Rs. 399", R.drawable.r8),
        Restaurant("Coffee House", "4.3", "Rs. 249", R.drawable.r9)

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

        return view
    }

}
