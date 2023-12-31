package com.example.adapter

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
    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

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

        // fix bug
        val photoURL = listShop.getphotoShop()
        if (!photoURL.isNullOrEmpty()) {
            val photo : Uri = Uri.parse(photoURL)
            Picasso.get().load(photo).into(holder.ivphotoShop)
        } //fix bug

        holder.tvnamaToko.text = listShop.getShopName()
        holder.tvOwner.text = listShop.getowner()
        holder.phone.text = listShop.getPhone()
        holder.tvAddress.text = listShop.getAddress()

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(shopList[holder.adapterPosition])
        }

//        Picasso.get().load(photo).into(holder.ivphotoShop)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ShopOwner)
    }

    override fun getItemCount(): Int = shopList.size

}