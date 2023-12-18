package com.example.foodection

class ShopOwner {
    private var shopName: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var address: String? = null
    private var phone: String? = null
    private var description: String? = null
    private var userID: String? = null
    private var userName: String? = null
    private var photoShop : String? = null
    private var owner : String? = null

    companion object {
        private var instance: ShopOwner? = null

        fun getInstance(): ShopOwner {
            if (instance == null) {
                instance = ShopOwner()
            }
            return instance!!
        }
    }

    fun getShopName(): String?{
        return shopName
    }

    fun setShopName(shopName : String?){
        this.shopName = shopName
    }

    fun getLatitude(): String?{
        return latitude
    }

    fun setLatitude(latitude : String?){
        this.latitude = latitude
    }

    fun getLongitude(): String?{
        return longitude
    }

    fun setLongitude(longitude : String?){
        this.longitude = longitude
    }

    fun getAddress(): String?{
        return address
    }

    fun setAddress(address : String?){
        this.address = address
    }

    fun getPhone(): String?{
        return phone
    }

    fun setPhone(phone : String?){
        this.phone = phone
    }

    fun getDescription(): String?{
        return description
    }

    fun setDescription(description : String?){
        this.description = description
    }

    fun getUserID(): String?{
        return userID
    }

    fun setUserID(userID : String?){
        this.userID = userID
    }

    fun getUserName(): String?{
        return userName
    }

    fun setUserName(userName : String?){
        this.userName= userName
    }

    fun getphotoShop(): String?{
        return photoShop
    }

    fun setphotoShop(photoShop : String?){
        this.photoShop= photoShop
    }

    fun getowner(): String?{
        return owner
    }

    fun setowner(owner : String?){
        this.owner = owner
    }

    operator fun component1() = shopName
    operator fun component2() = owner
    operator fun component3() = phone
    operator fun component4() = address
    operator fun component5() = photoShop
}