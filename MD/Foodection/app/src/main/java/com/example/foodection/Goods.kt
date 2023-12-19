package com.example.foodection

class Goods {
    private var goodsName: String? = null
    private var goodsPrice : String? = null
    private var goodsStock : String? = null
    private var goodsPhoto : String? = null
    private var username : String? = null
    private var userID: String? = null
    private var owner: String? = null

    companion object {
        private var instance: Goods? = null

        fun getInstance(): Goods {
            if (instance == null) {
                instance = Goods()
            }
            return instance!!
        }
    }

    fun getgoodsName(): String?{
        return goodsName
    }

    fun setgoodsName(goodsName : String?){
        this.goodsName = goodsName
    }

    fun getgoodsPrice(): String?{
        return goodsPrice
    }

    fun setgoodsPrice(goodsPrice : String?){
        this.goodsPrice = goodsPrice
    }

    fun getgoodsStock(): String?{
        return goodsStock
    }

    fun setgoodsStock(goodsStock: String?){
        this.goodsStock = goodsStock
    }

    fun getgoodsPhoto(): String?{
        return goodsPhoto
    }

    fun setgoodsPhoto(goodsPhoto: String?){
        this.goodsPhoto = goodsPhoto
    }

    fun getgoodsUsername(): String?{
        return username
    }

    fun setgoodsUsername(username: String?){
        this.username = username
    }

    fun getgoodsUserID(): String?{
        return userID
    }

    fun setgoodsUserID(userID: String?){
        this.userID = userID
    }

    fun getgoodsOwner(): String?{
        return owner
    }

    fun setgoodsOwner(owner: String?){
        this.owner = owner
    }

}