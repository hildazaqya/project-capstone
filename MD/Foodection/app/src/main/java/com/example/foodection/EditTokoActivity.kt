package com.example.foodection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.foodection.databinding.ActivityEditTokoBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class EditTokoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditTokoBinding
    private val galleryCode = 1

    private lateinit var auth: FirebaseAuth
    private var storage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference
    private var db = Firebase.firestore

    private var pathPhotoShop : String? = null
    private val username = Account.getInstance().getuserName()
    private val userID = Account.getInstance().getUserID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        val userID = Account.getInstance().getUserID().toString()
        loadToko(userID)

        binding.buttonEditToko.setOnClickListener{
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

            db.collection("Toko").whereEqualTo("userID", userID).get().addOnCompleteListener{task ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    for(document in task.result!!){
                            val docID = document.id
                        if (pathPhoto == null){
                            db.collection("Toko").document(docID).update(
                                "Shop Name",shopName,
                                "latitude", latitude,
                                "longitude",longitude,
                                "address", address,
                                "phone", phone,
                                "description", description,
                            ).addOnCompleteListener {
                                showToast("Data Toko Berhasil Diubah!")
                            }
                        }else{
                            db.collection("Toko").document(docID).update(
                                "Shop Name",shopName,
                                "latitude", latitude,
                                "longitude",longitude,
                                "address", address,
                                "phone", phone,
                                "description", description,
                                "photoShop", pathPhoto
                            ).addOnCompleteListener {
                                showToast("Data Toko Berhasil Diubah!")
                            }
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

                val filepath: StorageReference = storageRef.child("fotoToko-Update")
                    .child("fotoToko-$username--$userID-${Timestamp.now().nanoseconds}")
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

    private fun loadToko(userID: String){
        db.collection("Toko").whereEqualTo("userID", userID)
            .addSnapshotListener{value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        binding.inputNamaToko.setText(snapshot.getString("Shop Name"))
                        binding.inputLatitude.setText(snapshot.getString("latitude"))
                        binding.inputLongitude.setText(snapshot.getString("longitude"))
                        binding.inputAlamatToko.setText(snapshot.getString("address"))
                        binding.inputNomorTelepon.setText(snapshot.getString("phone"))
                        binding.inputDeskripsi.setText(snapshot.getString("description"))

                        val pathPhotoOld = snapshot.getString("photoShop")
                        val photoOld : Uri = Uri.parse(pathPhotoOld)
                        Picasso.get().load(photoOld).into(binding.postImageView)
                    }
                }
            }
    }


}