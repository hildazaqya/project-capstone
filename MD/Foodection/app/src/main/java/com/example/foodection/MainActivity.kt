package com.example.foodection

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.foodection.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var photoProfile : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermission()

        // Firebase
        auth = FirebaseAuth.getInstance()

        val userID = Account.getInstance().getUserID().toString()
        val jenisAkun = Account.getInstance().getjenisAkun().toString()
        loadDatabase(userID,jenisAkun)

        binding.liveScanBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, LiveScanActivity::class.java ))
        }

        binding.profile.setOnClickListener {
            if (jenisAkun == "Pengguna"){
                startActivity(Intent(this@MainActivity, ProfileUserActivity::class.java))
            }else if(jenisAkun == "Pemilik"){
                startActivity(Intent(this@MainActivity, ProfileOwnerActivity::class.java))
            }
        }

        binding.cariToko.setOnClickListener{
            startActivity(Intent(this@MainActivity, CariTokoActivity::class.java))
        }

    }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }

    private fun loadDatabase(userID : String, jenisAkun: String){
        db.collection(jenisAkun).whereEqualTo("userID", userID)
            .addSnapshotListener{ value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        binding.userName.text = snapshot.getString("nama")
                        val photoPath = snapshot.getString("foto profil")
                        photoProfile = Uri.parse(photoPath)
                        Picasso.get().load(photoProfile).into(binding.profile)
                    }

                }
            }
    }
}