package com.example.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dummy.Vegetables
import com.example.foodection.DetailItemActivity
import com.example.foodection.R

class CariSayurAdapter (private val listCariSayur: List<Vegetables>) : RecyclerView.Adapter<CariSayurAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cari_sayur, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo, price, kandungan, detail) = listCariSayur[position]
        holder.tvName.text = name
        holder.tvHarga.text = price
        holder.imgPhoto.setImageResource(photo)
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener {
            val intentDetailFood = Intent(holder.itemView.context, DetailItemActivity::class.java)
            intentDetailFood.putExtra(DetailItemActivity.SET, listCariSayur[position])
            holder.itemView.context.startActivity(intentDetailFood)
        }
    }

    override fun getItemCount(): Int = listCariSayur.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imsayur)
        val tvName: TextView = itemView.findViewById(R.id.tvnamasayur)
        val tvHarga: TextView = itemView.findViewById(R.id.tvharga)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Vegetables)
    }
}