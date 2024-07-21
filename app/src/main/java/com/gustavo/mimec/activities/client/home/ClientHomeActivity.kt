package com.gustavo.mimec.activities.client.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.MainActivity
import com.gustavo.mimec.models.User
import com.gustavo.mimec.utils.SharedPref

class ClientHomeActivity : AppCompatActivity() {

    private val TAG = "ClientHomeActivity"
    var buttonLogout: Button? = null
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_home)
        sharedPref = SharedPref(this)
        buttonLogout = findViewById(R.id.btn_logout)
        buttonLogout?.setOnClickListener {logout()}

        getUserUserFromSession()
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent (this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG, "User: $user")
        }
    }
}




