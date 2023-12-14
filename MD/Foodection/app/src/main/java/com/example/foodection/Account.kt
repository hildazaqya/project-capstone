package com.example.foodection

class Account private constructor(){
    private var userID: String? = null
    private var nama: String? = null
    private var email: String? = null
    private var fotoprofil: String? = null
    private var nomorTelp: String? = null
    private var jenisAkun : String? = null
    private var userName : String? = null

    companion object {
        private var instance: Account? = null

        fun getInstance(): Account {
            if (instance == null) {
                instance = Account()
            }
            return instance!!
        }
    }

    fun getUserID(): String? {
        return userID
    }

    fun setUserID(userID: String?) {
        this.userID = userID
    }

    fun getNama(): String? {
        return nama
    }

    fun setNama(nama: String?) {
        this.nama = nama
    }

    fun getjenisAkun(): String? {
        return jenisAkun
    }

    fun setjenisAkun(jenisAkun: String?) {
        this.jenisAkun = jenisAkun
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getFotoprofil(): String? {
        return fotoprofil
    }

    fun setFotoprofil(fotoprofil: String?) {
        this.fotoprofil = fotoprofil
    }

    fun getNomortelp(): String? {
        return nomorTelp
    }

    fun setNomortelp(nomortelp: String?) {
        this.nomorTelp = nomortelp
        }

    fun getuserName(): String? {
        return userName
    }

    fun setuserName(userName: String?) {
        this.userName = userName
    }
}