package com.gustavo.mimec.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.adapters.RolesAdapter
import com.gustavo.mimec.models.User
import com.gustavo.mimec.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    var recyclerViewRoles: RecyclerView? = null
    var user: User? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getUserUserFromSession()

        adapter = RolesAdapter(this ,user?.roles!!)
        recyclerViewRoles?.adapter = adapter
    }

    private fun getUserUserFromSession(){

        val sharedPref = SharedPref(this)
        val gson = Gson()

        if (!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}