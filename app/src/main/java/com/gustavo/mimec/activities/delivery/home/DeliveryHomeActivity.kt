package com.gustavo.mimec.activities.delivery.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.MainActivity
import com.gustavo.mimec.fragments.client.ClientCategorysFragment
import com.gustavo.mimec.fragments.client.ClientOrdersFragment
import com.gustavo.mimec.fragments.client.ClientProfileFragment
import com.gustavo.mimec.fragments.delivery.DeliveryOrdersFragment
import com.gustavo.mimec.fragments.taller.TallerCategoryFragment
import com.gustavo.mimec.fragments.taller.TallerOrdersFragment
import com.gustavo.mimec.fragments.taller.TallerProductFragment
import com.gustavo.mimec.models.User
import com.gustavo.mimec.utils.SharedPref

class DeliveryHomeActivity : AppCompatActivity() {

    private val TAG = "DeliveryHomeActivity"
    /*var buttonLogout: Button? = null*/
    var sharedPref: SharedPref? = null

    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delivery_home)
        sharedPref = SharedPref(this)
        /* buttonLogout = findViewById(R.id.btn_logout)
         buttonLogout?.setOnClickListener {logout()}*/


        openFragment(DeliveryOrdersFragment())
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_orders -> {
                    openFragment(DeliveryOrdersFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }

                else -> false

            }
        }

        getUserUserFromSession()
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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