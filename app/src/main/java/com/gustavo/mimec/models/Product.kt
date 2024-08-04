package com.gustavo.mimec.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image1") val image1: String? = null,
    @SerializedName("image2") val image2: String? = null,
    @SerializedName("image3") val image3: String? = null,
    @SerializedName("id_category") val idCategory: String,
    @SerializedName("quantity") val quantity: String? = null,

) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(id='$id', name='$name', description='$description', price=$price, image1='$image1', image2='$image2', image3='$image3', idCategory='$idCategory', quantity='$quantity')"
    }


}