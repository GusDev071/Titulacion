package com.gustavo.mimec.routes


import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UsersRoutes {

    @POST("users/create")
    fun register(@Body user: User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("users/login")
    fun login (@Field("email") email: String, @Field("password") password: String): Call<ResponseHttp>

    @Multipart
    @PUT("users/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token: String
    ):Call<ResponseHttp>

    @PUT("users/updateWithoutImage")
    fun updateWithoutImage(
       @Body user: User,
       @Header("Authorization") token: String
    ):Call<ResponseHttp>
}