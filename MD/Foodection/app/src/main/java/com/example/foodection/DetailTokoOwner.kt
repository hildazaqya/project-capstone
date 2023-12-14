package com.example.foodection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodection.databinding.ActivityDetailTokoOwnerBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class DetailTokoOwner : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTokoOwnerBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var photoShop : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTokoOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        val userID = Account.getInstance().getUserID().toString()
        loadDatabaseShop(userID)

        binding.pinEditEdit.setOnClickListener{
            startActivity(Intent(this@DetailTokoOwner, EditTokoActivity::class.java))
        }
    }

    private fun loadDatabaseShop(userID : String){
        db.collection("Toko").whereEqualTo("userID", userID)
            .addSnapshotListener{ value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        binding.namaDetailToko.text = snapshot.getString("Shop Name")
                        binding.namaPemilikDetailToko.text = snapshot.getString("ownerShop")
                        binding.notelpPemilikDetailToko.text = snapshot.getString("phone")
                        binding.alamatDetailToko.text = snapshot.getString("address")
                        binding.txtDetailToko.text = snapshot.getString("description")

                        val photoPath = snapshot.getString("photoShop")
                        photoShop = Uri.parse(photoPath)
                        Picasso.get().load(photoShop).into(binding.gambarDetailToko)
                    }
                }
            }
    }
}