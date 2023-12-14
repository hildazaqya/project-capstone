package com.example.foodection

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodection.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val radioGroup = binding.toggle

        radioGroup.setOnCheckedChangeListener { _, i ->
            val jenisAkun: String = when (i) {
                R.id.pengguna -> {
                    showToastAndReturn("Pengguna")
                }
                R.id.pemilik ->{
                    showToastAndReturn("Pemilik")
                }
                else ->{
                    showToastAndReturn("Tidak Ada Terpilih")
                }

        }

        binding.btnRegister.setOnClickListener {
            showToast(jenisAkun)
            val username = binding.regisUsername.text.toString()
            val email = binding.regisEmail.text.toString()
            val notelp = binding.regisNotelp.text.toString()
            val password = binding.regisPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        assert(user != null)
                        val userID: String = user!!.uid

                        val account = hashMapOf(
                            "userID" to userID,
                            "username" to username,
                            "email" to email,
                            "notelp" to notelp,
                            "password" to password,
                            "jenis akun" to jenisAkun,
                            "nama" to null,
                            "foto profil" to null
                        )

                        val akun = Account.getInstance()
                        akun.setUserID(userID)
                        akun.setuserName(username)
                        akun.setEmail(email)
                        akun.setNomortelp(notelp)
                        akun.setjenisAkun(jenisAkun)
                        akun.setNama(null)
                        akun.setFotoprofil(null)

                        if (jenisAkun == "Pengguna") {
                            db.collection("Pengguna")
                                .add(account)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        }else if (jenisAkun == "Pemilik"){
                            db.collection("Pemilik")
                                .add(account)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        ContentValues.TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(ContentValues.TAG, "Error adding document", e)
                                }
                        }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
            }
        }
    }

    private fun showToastAndReturn(message: String): String {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        return message
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}