package com.example.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodection.R
import com.example.foodection.ShopOwner
import com.squareup.picasso.Picasso

class ShopAdapter(private val shopList : ArrayList<ShopOwner>) : RecyclerView.Adapter<ShopAdapter.ListViewHolder>(){
    var context: Context? = null

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvnamaToko: TextView = itemView.findViewById(R.id.tvnamaToko)
        val tvOwner: TextView = itemView.findViewById(R.id.tvownerShop)
        val phone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val ivphotoShop: ImageView = itemView.findViewById(R.id.imageShop)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.shoplist_row, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listShop = shopList[position]
        val photoURL = listShop.getphotoShop()
        val photo : Uri = Uri.parse(photoURL)
        holder.tvnamaToko.text = listShop.getShopName()
        holder.tvOwner.text = listShop.getowner()
        holder.phone.text = listShop.getPhone()
        holder.tvAddress.text = listShop.getAddress()

        Picasso.get().load(photo).into(holder.ivphotoShop)
    }

    override fun getItemCount(): Int = shopList.size

}