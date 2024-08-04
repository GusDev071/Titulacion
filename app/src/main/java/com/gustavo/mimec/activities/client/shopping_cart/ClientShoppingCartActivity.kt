package com.gustavo.mimec.activities.client.shopping_cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.address.create.ClientAddressCreateActivity
import com.gustavo.mimec.activities.client.address.list.ClientAddressListActivity
import com.gustavo.mimec.adapters.ShppingCartAdapter
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.utils.SharedPref

class ClientShoppingCartActivity : AppCompatActivity() {

    var reciclerViewShoppingCart: RecyclerView? = null
    var txtViewTotal: TextView? = null
    var btnNext: Button? = null
    var toolbar: Toolbar? = null

    var adapter: ShppingCartAdapter? = null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_shopping_cart)

        sharedPref = SharedPref(this)

        reciclerViewShoppingCart = findViewById(R.id.rvShoppingCart)
        txtViewTotal = findViewById(R.id.tvTotal)
        btnNext = findViewById(R.id.btnNext)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(resources.getColor(R.color.black))
        toolbar?.title = "Tus servicios seleccionados"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reciclerViewShoppingCart?.layoutManager = LinearLayoutManager(this)

        getProductsFromSharedPref()

        btnNext?.setOnClickListener{goToCreateList()}

    }

    private fun goToCreateList() {
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun setTotal(total: Double){
        txtViewTotal?.text = "$${total}"
    }

    private fun getProductsFromSharedPref(){
        if (!sharedPref?.getData("order").isNullOrBlank()){ //valida si existe una orden en sharedPref
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)

            adapter = ShppingCartAdapter(this, selectedProducts)
            reciclerViewShoppingCart?.adapter = adapter

        }
    }
}