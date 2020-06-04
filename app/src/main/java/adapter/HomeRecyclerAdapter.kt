package adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pratishtha.foodrunner.R
import com.squareup.picasso.Picasso
import model.Restaurant

class HomeRecyclerAdapter(val context:Context,val itemList:ArrayList<Restaurant>) :RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){

        val rlContent:RelativeLayout=view.findViewById(R.id.rlContent)

        val txtRestaurantName:TextView=view.findViewById(R.id.txtRestaurantName)
        val txtPrice:TextView=view.findViewById(R.id.txtPrice)
        val txtRating:TextView=view.findViewById(R.id.txtRating)
        val imgRestaurantImage: ImageView =view.findViewById(R.id.imgRestaurantImage)
        val imgRupees:ImageView=view.findViewById(R.id.imgRupees)
        val imgFav:ImageView=view.findViewById(R.id.imgFav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant=itemList[position]
        holder.txtRestaurantName.text= restaurant.restaurantName
        holder.txtRating.text=restaurant.restaurantRating
        holder.txtPrice.text=restaurant.restaurantCost_For_One
       // holder.imgRestaurantImage.setImageResource(restaurant.restaurantImage)

        Picasso.get().load(restaurant.restaurantImage).into(holder.imgRestaurantImage)

        holder.rlContent.setOnClickListener{
            Toast.makeText(context,"Clicked on ${holder.txtRestaurantName.text}",Toast.LENGTH_SHORT).show()
        }
    }
}