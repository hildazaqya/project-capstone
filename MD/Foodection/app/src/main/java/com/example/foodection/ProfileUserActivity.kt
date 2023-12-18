package com.example.foodection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.foodection.databinding.ActivityProfileUserBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ProfileUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileUserBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private var storage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private lateinit var photoProfile : Uri
    private var pathPhotoProfile : String? = null
    private var userID : String? = null

    private val galleryCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()

        binding.changePhoto.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, galleryCode)
        }

        userID = Account.getInstance().getUserID().toString()
        val idUser = Account.getInstance().getUserID().toString()
        val jenisAkun = Account.getInstance().getjenisAkun().toString()
        loadDatabase(idUser,jenisAkun)

        binding.btnSave.setOnClickListener{
            val nama = binding.edtProfileNama.text.toString().trim()
            val phone = binding.edtProfileNotelp.text.toString().trim()
            val pathPhoto = pathPhotoProfile

            val user = auth.currentUser
            assert(user != null)

            db.collection(jenisAkun).whereEqualTo("userID", userID).get().addOnCompleteListener{ task ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    for(document in task.result!!){
                        val docID = document.id
                        if (pathPhoto == null){
                            db.collection(jenisAkun).document(docID).update("nama", nama, "notelp", phone)
                        }else{
                            db.collection(jenisAkun).document(docID).update("nama", nama, "notelp", phone, "foto profil", pathPhoto)
                        }
                        showToast("Data kamu berhasil diperbarui!")
                        binding.userName.text = nama
                        binding.profileNotelp.text = phone
                    }
                }
            }.addOnFailureListener{ exception ->
                showToast(exception.toString())
            }
        }
    }

    private fun loadDatabase(userID : String, jenisAkun: String){
        db.collection(jenisAkun).whereEqualTo("userID", userID)
            .addSnapshotListener{ value, _ ->
                assert(value != null)

                if (!value!!.isEmpty) {
                    for (snapshot in value) {
                        binding.userName.text = snapshot.getString("nama")
                        binding.profileNotelp.text = snapshot.getString("notelp")
                        val photoPath = snapshot.getString("foto profil")
                        photoProfile = Uri.parse(photoPath)
                        Picasso.get().load(photoProfile).into(binding.imageProfile)
                    }
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == galleryCode && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                binding.imageProfile.setImageURI(selectedImageUri)

                val filepath: StorageReference = storageRef.child("fotoPengguna-Update")
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