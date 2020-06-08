

package adapter

import activity.DescriptionActivity
import android.content.Context
import android.content.Intent
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

        val txtRestaurantName:TextView=view.findViewById(R.id.txtRestaurantName)
        val txtPrice:TextView=view.findViewById(R.id.txtPrice)
        val txtRating:TextView=view.findViewById(R.id.txtRating)
        val imgRestaurantImage: ImageView =view.findViewById(R.id.imgRestaurantImage)

        val rlContent:RelativeLayout=view.findViewById(R.id.rlContent)

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

        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.default_restaurant_image).into(holder.imgRestaurantImage)

        holder.rlContent.setOnClickListener{
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("restaurant_id",restaurant.restaurantId)
            intent.putExtra("restaurant_name",restaurant.restaurantName)
            context.startActivity(intent)
        }
    }
}