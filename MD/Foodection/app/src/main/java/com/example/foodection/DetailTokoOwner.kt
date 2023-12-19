package com.example.foodection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.GoodsAdapter
import com.example.foodection.databinding.ActivityDetailTokoOwnerBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class DetailTokoOwner : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTokoOwnerBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private val goodsList = ArrayList<Goods>()

    private lateinit var username : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTokoOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        val userID = Account.getInstance().getUserID().toString()
        loadDatabaseShop(userID)

        //TAMBAH
        binding.rvListGoods.setHasFixedSize(true)
        loadDataBarang(userID)

        binding.pinEditEdit.setOnClickListener{
            startActivity(Intent(this@DetailTokoOwner, EditTokoActivity::class.java))
        }

        //Tambah
        binding.buttonTambahBarang.setOnClickListener{
            startActivity(Intent(this@DetailTokoOwner,AddGoodsActivity::class.java))
        }

        //Tambah
        binding.pinHapusToko.setOnClickListener{
            val deleteShop  = hashMapOf<String,Any>(
                "address" to FieldValue.delete(),
                "description" to FieldValue.delete(),
                "latitude" to FieldValue.delete(),
                "longitude" to FieldValue.delete(),
                "owner" to FieldValue.delete(),
                "photoShop" to FieldValue.delete(),
                "phone" to FieldValue.delete()
            )

            db.collection("Toko").whereEqualTo("userID", userID).get().addOnCompleteListener{task ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    for (document in task.result!!){
                        val docID = document.id
                        db.collection("Toko").document(docID).update(deleteShop)
                        db.collection("Toko").document(docID).update("shopName", null)
                            .addOnCompleteListener{
                                Toast.makeText(this, "Toko berhasil Dihapus!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }

            db.collection("Barang").whereEqualTo("userID", userID).get().addOnCompleteListener { task ->
                if (task.isSuccessful && !task.result?.isEmpty!!) {
                    for (document in task.result!!) {
                        val docID = document.id
                        db.collection("Barang").document(docID).delete().addOnCompleteListener {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun loadDatabaseShop(userID : String){
        db.collection("Toko").whereEqualTo("userID", userID)
            .addSnapshotListener{ value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        binding.namaDetailToko.text = snapshot.getString("shopName")
                        binding.namaPemilikDetailToko.text = snapshot.getString("owner")
                        binding.notelpPemilikDetailToko.text = snapshot.getString("phone")
                        binding.alamatDetailToko.text = snapshot.getString("address")
                        binding.txtDetailToko.text = snapshot.getString("description")

                        username = snapshot.getString("username").toString()

                        val photoPath = snapshot.getString("photoShop")
                        if (photoPath == null){
                            finish()
                        }else{
                            val photoShop = Uri.parse(photoPath)
                            Picasso.get().load(photoShop).into(binding.gambarDetailToko)
                        }
                    }
                }
            }
    }

    //INI KU TAMBAH
    private fun loadDataBarang(userID : String){
        db.collection("Barang").whereEqualTo("userID", userID).get().addOnSuccessListener {queryDocumentSnapshots ->
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

    // Tambahan
    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}