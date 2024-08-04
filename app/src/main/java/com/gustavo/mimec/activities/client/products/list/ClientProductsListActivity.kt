package com.gustavo.mimec.activities.client.products.list

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.adapters.ProductsAdapter
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.ProductsProvider
import com.gustavo.mimec.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {

    var reciclerViewProducts: RecyclerView? = null
    var adapter: ProductsAdapter? = null
    var user: User? = null
    var productsProvider: ProductsProvider? = null
    var products: ArrayList<Product>? = ArrayList()
    val TAG = "ClientProducts"
    var idCategory: String? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_products_list)

        sharedPref = SharedPref(this)
        idCategory = intent.getStringExtra("idCategory")

        getUserUserFromSession()
        productsProvider = ProductsProvider(user?.SessionToken!!)

        reciclerViewProducts = findViewById(R.id.recyclerview_products)
        reciclerViewProducts?.layoutManager = GridLayoutManager(this, 2)

        getProducts()

    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.body() != null){
                    products = response.body()
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products!!)
                    reciclerViewProducts?.adapter = adapter

                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
               Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error: ${t.message}")
            }

        })
    }
}