package com.gustavo.mimec.providers

import com.gustavo.mimec.api.ApiRoutes
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {

    private var usersRoutes: UsersRoutes? = null

    init {
        val api = ApiRoutes()
        usersRoutes = api.getUsersRoutes()
    }

    fun register(user: User): Call<ResponseHttp>? {
        return usersRoutes?.register(user)
    }

}