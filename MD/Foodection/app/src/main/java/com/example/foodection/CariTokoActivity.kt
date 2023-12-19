package com.example.foodection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.ShopAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class CariTokoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var rvShopLists : RecyclerView
    private val shopList = ArrayList<ShopOwner>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cari_toko)

        auth = FirebaseAuth.getInstance()

        rvShopLists = findViewById(R.id.toko_list)
        rvShopLists.setHasFixedSize(true)

        loadDataToko()
    }

    private fun loadDataToko(){
        db.collection("Toko").get().addOnSuccessListener { queryDocumentSnapshots ->
            if(!queryDocumentSnapshots.isEmpty){
                for (shops in queryDocumentSnapshots){
                    val listShop = shops.toObject(ShopOwner::class.java)
                    shopList.add(listShop)
                }
            }

            rvShopLists.layoutManager = LinearLayoutManager(this)
            val shopListAdapter = ShopAdapter(shopList)
            rvShopLists.adapter = shopListAdapter

            shopListAdapter.setOnItemClickCallback(object : ShopAdapter.OnItemClickCallback{
                override fun onItemClicked(data: ShopOwner) {
                    showSelectedShop(data)
                }
            })
        }
    }

    private fun showSelectedShop(shopList : ShopOwner){
        val gotoDetail = Intent(this@CariTokoActivity, DetailTokoUserActivity::class.java)
        ShopOwner.getInstance().setUserName(shopList.getUserName())
        ShopOwner.getInstance().setPhone(shopList.getPhone())
        startActivity(gotoDetail)
    }
}