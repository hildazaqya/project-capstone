package com.example.foodection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.GoodsAdapter
import com.example.foodection.databinding.ActivityDetailTokoUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class DetailTokoUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTokoUserBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var photoShop : Uri
    private val goodsList = ArrayList<Goods>()

//    private lateinit var phone : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTokoUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        val usernameOwner = ShopOwner.getInstance().getUserName()
        val ownerNumber = ShopOwner.getInstance().getPhone().toString()

        if (usernameOwner!=null){
            loadShop(usernameOwner)
            loadDataBarang(usernameOwner)
        }

        //TAMBAH
        binding.rvListGoods.setHasFixedSize(true)

        // INI KU EDIT
        binding.pinMapDetailToko.setOnClickListener{
            val gotoLocation =  Intent(this@DetailTokoUserActivity, MapsActivity::class.java)
            gotoLocation.putExtra("fromDetail", true)
            startActivity(gotoLocation)
        }

        //Tambahan
        binding.pinTelpToko.setOnClickListener{
            val callOwner = Intent(Intent.ACTION_DIAL)
            callOwner.data = Uri.parse("tel:$ownerNumber")
            startActivity(callOwner)
        }
    }

    private fun loadShop(username: String?){
        db.collection("Toko").whereEqualTo("username", username)
            .addSnapshotListener{ value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        //Ubah
                        binding.namaDetailToko.text = snapshot.getString("shopName")
                        binding.namaPemilikDetailToko.text = snapshot.getString("owner")
                        binding.notelpPemilikDetailToko.text = snapshot.getString("phone")
                        binding.alamatDetailToko.text = snapshot.getString("address")
                        binding.deskripsiTokoDetail.text = snapshot.getString("description")

                        val photoPath = snapshot.getString("photoShop")
                        photoShop = Uri.parse(photoPath)
                        Picasso.get().load(photoShop).into(binding.gambarDetailToko)
                    }
                }
            }
    }

    //INI KU TAMBAH
    private fun loadDataBarang(username : String){
        db.collection("Barang").whereEqualTo("username",username).get().addOnSuccessListener {queryDocumentSnapshots ->
            if(!queryDocumentSnapshots.isEmpty){
                for (goods in queryDocumentSnapshots){
                    val listGoods = goods.toObject(Goods::class.java)
                    goodsList.add(listGoods)
                }
            }

            binding.rvListGoods.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val listGoodsAdapter = GoodsAdapter(goodsList)
            binding.rvListGoods.adapter = listGoodsAdapter
        }
    }
}