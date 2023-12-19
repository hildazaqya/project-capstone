package com.example.foodection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.foodection.databinding.ActivityAddGoodsBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddGoodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoodsBinding

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private var storage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private var pathPhotoGoods : String? = null
    private val username = Account.getInstance().getuserName()
    private val userID = Account.getInstance().getUserID()
    private val galleryCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase
        auth = FirebaseAuth.getInstance()
        val userID = Account.getInstance().getUserID().toString()
        val namaOwner = Account.getInstance().getNama().toString()

        binding.buttonTambahBarang.setOnClickListener{
            val goodsName = binding.inputNamaBarang.text.toString().trim()
            val price = binding.inputhargaBarang.text.toString().trim()
            val goodsStock = binding.inputstokBarang.text.toString().trim()
            val pathPhoto = pathPhotoGoods
            val goodsPrice = "Rp.$price"

            val user = auth.currentUser
            assert(user!=null)

            val goods = Goods.getInstance()
            goods.setgoodsName(goodsName)
            goods.setgoodsPrice(goodsPrice)
            goods.setgoodsStock(goodsStock)
            goods.setgoodsPhoto(pathPhoto)
            goods.setgoodsOwner(namaOwner)
            goods.setgoodsUsername(username)
            goods.setgoodsUserID(userID)

            val addGoods = hashMapOf(
                "userID" to userID,
                "username" to username,
                "owner" to namaOwner,
                "goodsName" to goodsName,
                "goodsPrice" to goodsPrice,
                "goodsStock" to goodsStock,
                "goodsPhoto" to pathPhoto
            )

            db.collection("Barang").add(addGoods).addOnSuccessListener {
                showToast("Tambah Barang Sukses!")
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

                val filepath: StorageReference = storageRef.child("fotoBarang")
                    .child("fotoBarang-$username-$userID-${Timestamp.now().nanoseconds}")
                if (selectedImageUri != null) {
                    filepath.putFile(selectedImageUri).addOnSuccessListener {
                        filepath.downloadUrl.addOnSuccessListener { uri ->
                            pathPhotoGoods = uri.toString()
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
}