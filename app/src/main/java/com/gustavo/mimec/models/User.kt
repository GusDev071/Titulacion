package com.gustavo.mimec.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("image") val image: String? = null,
    @SerializedName("session_token") val SessionToken: String? = null,
    @SerializedName("is_available") val isAvailable: Boolean?= null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null
){
    override fun toString(): String {
        return "User(id=$id, name='$name', lastname='$lastname', email='$email', phone='$phone', password='$password', image=$image, SessionToken=$SessionToken, isAvailable=$isAvailable, roles=$roles)"
    }
}

