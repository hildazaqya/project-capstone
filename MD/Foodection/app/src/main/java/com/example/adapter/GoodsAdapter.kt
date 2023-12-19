package com.example.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodection.Goods
import com.example.foodection.R
import com.squareup.picasso.Picasso

class GoodsAdapter(private val goodsList: ArrayList<Goods>): RecyclerView.Adapter<GoodsAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvgoodsName : TextView = itemView.findViewById(R.id.tvgoodsName)
        val tvgoodsPrice : TextView = itemView.findViewById(R.id.tvgoodsPrice)
        val tvgoodsStock: TextView = itemView.findViewById(R.id.tvgoodsStock)
        val ivphotoGoods : ImageView = itemView.findViewById(R.id.ivgoodsImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goodslist_horizontal, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = goodsList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listGoods = goodsList[position]

        val photoURL = listGoods.getgoodsPhoto()
        val photo : Uri = Uri.parse(photoURL)

        holder.tvgoodsName.text = listGoods.getgoodsName()
        holder.tvgoodsPrice.text = listGoods.getgoodsPrice()
        holder.tvgoodsStock.text = "Stok: " + listGoods.getgoodsStock()
        Picasso.get().load(photo).into(holder.ivphotoGoods)
    }
}