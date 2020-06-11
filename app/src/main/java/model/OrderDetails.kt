package model

import org.json.JSONArray

data class OrderDetails(
    val orderId:String,
    val resName:String,
    val orderDate:String,
    val foodItem:JSONArray
)