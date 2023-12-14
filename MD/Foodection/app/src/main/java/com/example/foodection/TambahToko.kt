package com.example.foodection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.foodection.databinding.ActivityTambahTokoBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TambahToko : AppCompatActivity() {

    private lateinit var binding: ActivityTambahTokoBinding
    private lateinit var auth: FirebaseAuth
    private var storage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference
    private var db = Firebase.firestore

    private var pathPhotoShop : String? = null
    private val username = Account.getInstance().getuserName()
    private val userID = Account.getInstance().getUserID()
    private val galleryCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        val userID = Account.getInstance().getUserID().toString()
        val nameOwner = Account.getInstance().getNama().toString()

        binding.buttonTambahToko.setOnClickListener{
            val shopName = binding.inputNamaToko.text.toString().trim()
            val latitude = binding.inputLatitude.text.toString().trim()
            val longitude = binding.inputLongitude.text.toString().trim()
            val address = binding.inputAlamatToko.text.toString().trim()
            val phone = binding.inputNomorTelepon.text.toString().trim()
            val description = binding.inputDeskripsi.text.toString().trim()
            val pathPhoto = pathPhotoShop

            val user = auth.currentUser
            assert(user != null)

            val shop = ShopOwner.getInstance()
            shop.setShopName(shopName)
            shop.setLatitude(latitude)
            shop.setLongitude(longitude)
            shop.setAddress(address)
            shop.setPhone(phone)
            shop.setDescription(description)
            shop.setphotoShop(pathPhoto)
            shop.setowner(nameOwner)

            db.collection("Toko").whereEqualTo("userID", userID).get().addOnCompleteListener{task ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    for(document in task.result!!){
                        val docID = document.id
                        db.collection("Toko").document(docID).update(
                            "shopName",shopName,
                            "latitude", latitude,
                            "longitude",longitude,
                            "address", address,
                            "phone", phone,
                            "description", description,
                            "photoShop", pathPhoto,
                            "userID", userID,
                            "owner", nameOwner
                        ).addOnCompleteListener {
                            showToast("Data Toko Berhasil Ditambahkan!")
                        }
                    }
                }
            }
            finish()
        }

        binding.takePicture.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, galleryCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                binding.postImageView.setImageURI(selectedImageUri)

                val filepath: StorageReference = storageRef.child("fotoToko")
                    .child("fotoToko-$username--$userID${Timestamp.now().nanoseconds}")
                if (selectedImageUri != null) {
                    filepath.putFile(selectedImageUri).addOnSuccessListener {
                        filepath.downloadUrl.addOnSuccessListener { uri ->
                            pathPhotoShop = uri.toString()
                        }
                    }.addOnFailureListener { exception ->
                        showToast(exception.toString())
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    private fun updateUI(currentUser: FirebaseUser?) {
//        if (currentUser != null){
//            startActivity(Intent(this@TambahToko, DetailToko::class.java))
//        }
//    }
}