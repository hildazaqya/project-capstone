package com.example.foodection

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

        rvShopLists = findViewById(R.id.toko_list)
        rvShopLists.setHasFixedSize(true)

        loadDataToko()
    }

    private fun loadDataToko(){
        db.collection("Toko").get().addOnSuccessListener { queryDocumentSnapshots ->
            if(!queryDocumentSnapshots.isEmpty){
                for (Shops in queryDocumentSnapshots){
                    val listShop = Shops.toObject(ShopOwner::class.java)
                    shopList.add(listShop)
                }
            }

            rvShopLists.layoutManager = LinearLayoutManager(this)
            val shopListAdapter = ShopAdapter(shopList)
            rvShopLists.adapter = shopListAdapter
        }
    }
}