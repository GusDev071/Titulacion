package com.gustavo.mimec.routes


import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {

    @POST("users/create")
    fun register(@Body user: User): Call<ResponseHttp>

}