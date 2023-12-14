package com.example.foodection

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodection.databinding.ActivityCompleteProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CompleteProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteProfileBinding
    private lateinit var auth: FirebaseAuth
    private var storage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private var pathPhotoProfile : String? = null
    private var userID : String? = null

    private val galleryCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        val db = Firebase.firestore

        auth = FirebaseAuth.getInstance()

        // Ambil Data
        userID = Account.getInstance().getUserID().toString()
        val userName = Account.getInstance().getuserName().toString()
        val jenisAkun: String = Account.getInstance().getjenisAkun().toString()

        binding.btnChoose.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, galleryCode)
        }

        binding.btnSimpan.setOnClickListener{
            val nama = binding.edtProfileNama.text.toString().trim()
            val pathPhoto = pathPhotoProfile

            val user = auth.currentUser
            assert(user != null)

            db.collection(jenisAkun).whereEqualTo("userID", userID).get().addOnCompleteListener{ task ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    for(document in task.result!!){
                        val docID = document.id
                        db.collection(jenisAkun).document(docID).update("nama", nama, "foto profil", pathPhoto)

                        if (jenisAkun == "Pemilik"){
                            val toko = hashMapOf(
                                "userID" to userID,
                                "username" to userName,
                                "shopName" to null
                            )
                            val shop = ShopOwner.getInstance()
                            shop.setUserID(userID)
                            shop.setUserName(userName)
                            shop.setShopName(null)
                            db.collection("Toko").add(toko)
                        }
                    }
                }
            }
//            updateUI(user)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                binding.fotoProfile.setImageURI(selectedImageUri)

                val filepath: StorageReference = storageRef.child("fotoPengguna")
                    .child("fotoPengguna-$userID-${Timestamp.now().nanoseconds}")
                if (selectedImageUri != null) {
                    filepath.putFile(selectedImageUri).addOnSuccessListener {
                        filepath.downloadUrl.addOnSuccessListener { uri ->
                            pathPhotoProfile = uri.toString()
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