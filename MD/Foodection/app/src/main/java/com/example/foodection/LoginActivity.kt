package com.example.foodection

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodection.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radioGroup = binding.toggle

        val db = Firebase.firestore
        val pengguna: CollectionReference = db.collection("Pengguna")
        val pemilik: CollectionReference = db.collection("Pemilik")

        auth = FirebaseAuth.getInstance()

        radioGroup.setOnCheckedChangeListener { _, i ->
            val jenisLogin: String = when (i) {
                R.id.pengguna -> {
                    showToastAndReturn("Pengguna")
                }

                R.id.pemilik -> {
                    showToastAndReturn("Pemilik")
                }

                else -> {
                    showToastAndReturn("Tidak Ada Terpilih")
                }
            }

            binding.btnLogin.setOnClickListener {
                showToast(jenisLogin)
                val email = binding.loginEmail.text.toString()
                val password = binding.loginPassword.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            assert(user != null)
                            val userID: String = user!!.uid
                            if (jenisLogin == "Pengguna") {
                                pengguna.whereEqualTo("userID", userID)
                                    .addSnapshotListener { value, _ ->
                                        assert(value != null)
                                        if (!value!!.isEmpty) {
                                            for (snapshot in value) {
                                                val nama: String? = snapshot.getString("nama")
                                                val akun = Account.getInstance()
                                                akun.setUserID(snapshot.getString("userID"))
                                                akun.setuserName(snapshot.getString("username"))
                                                akun.setEmail(snapshot.getString("email"))
                                                akun.setNomortelp(snapshot.getString("notelp"))
                                                akun.setjenisAkun(snapshot.getString("jenis akun"))
                                                akun.setFotoprofil(snapshot.getString("foto profil"))
                                                akun.setNama(snapshot.getString("nama"))

                                                if (nama == null) {
                                                    updateProfile(user)
                                                    finish()
                                                } else {
                                                    updateUI(user)
                                                    finish()
                                                }
                                            }
                                        }
                                    }
                            } else if (jenisLogin == "Pemilik") {
                                pemilik.whereEqualTo("userID", userID)
                                    .addSnapshotListener { value, _ ->
                                        assert(value != null)
                                        if (!value!!.isEmpty) {
                                            for (snapshot in value) {
                                                val nama: String? = snapshot.getString("nama")
                                                val akun = Account.getInstance()
                                                akun.setUserID(snapshot.getString("userID"))
                                                akun.setuserName(snapshot.getString("username"))
                                                akun.setEmail(snapshot.getString("email"))
                                                akun.setNomortelp(snapshot.getString("notelp"))
                                                akun.setjenisAkun(snapshot.getString("jenis akun"))
                                                akun.setFotoprofil(snapshot.getString("foto profil"))
                                                akun.setNama(snapshot.getString("nama"))

                                                if (nama == null) {
                                                    updateProfile(user)
                                                    finish()
                                                } else {
                                                    updateUI(user)
                                                    finish()
                                                }
                                            }
                                        }
                                    }
                            }
                        } else {
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            showToast("Akun Tidak Terdaftar.")
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
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    private fun updateProfile(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, CompleteProfileActivity::class.java))
        }
    }
}
