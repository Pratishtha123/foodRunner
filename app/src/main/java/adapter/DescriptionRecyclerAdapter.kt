package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pratishtha.foodrunner.R
import model.Description
import model.Restaurant

class DescriptionRecyclerAdapter(val context: Context, val itemList:ArrayList<Description>) :RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>() {

    class DescriptionViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtCount: TextView =view.findViewById(R.id.txtCount)
        val txtDishName: TextView =view.findViewById(R.id.txtDishName)
        val txtPrice: TextView =view.findViewById(R.id.txtPrice)
        val btnAdd: Button =view.findViewById(R.id.btnAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_description_single_row,parent,false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
       val description=itemList[position]
        holder.txtDishName.text= description.dishName
        holder.txtPrice.text=description.dishPrice
        holder.btnAdd.setOnClickListener{
            Toast.makeText(context,"Clicked on ${holder.txtDishName.text}",Toast.LENGTH_SHORT).show()
        }
    }
}