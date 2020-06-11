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

class DescriptionRecyclerAdapter(val context: Context, val itemList:ArrayList<Description>, private val listener: OnItemClickListener
) :RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>() {

    companion object {
        var isCartEmpty = true
    }


    class DescriptionViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtCount: TextView =view.findViewById(R.id.txtCount)
        val txtDishName: TextView =view.findViewById(R.id.txtDishName)
        val txtPrice: TextView =view.findViewById(R.id.txtPrice)
        val btnAdd: Button =view.findViewById(R.id.btnAdd)
        val btnRemove:Button=view.findViewById(R.id.btnRemove)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_description_single_row,parent,false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    interface OnItemClickListener {
        fun onAddItemClick(dishObject:Description)
        fun onRemoveItemClick(dishObject:Description)
    }



    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val description = itemList[position]
        holder.txtDishName.text = description.dishName
        holder.txtPrice.text = description.dishPrice
        holder.txtCount.text = (position + 1).toString()

        holder.btnAdd.setOnClickListener {
            holder.btnRemove.visibility = View.VISIBLE
            holder.btnAdd.visibility = View.GONE
            listener.onAddItemClick(description)
        }

        holder.btnRemove.setOnClickListener {
            holder.btnRemove.visibility = View.GONE
            holder.btnAdd.visibility = View.VISIBLE
            listener.onRemoveItemClick(description)
        }
    }
        override fun getItemViewType(position: Int): Int {
            return position
        }

}

