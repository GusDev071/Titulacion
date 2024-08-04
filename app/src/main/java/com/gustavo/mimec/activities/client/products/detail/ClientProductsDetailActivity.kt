package com.gustavo.mimec.activities.client.products.detail

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gustavo.mimec.R
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.utils.SharedPref

class ClientProductsDetailActivity : AppCompatActivity() {

    var product: Product? = null
    var gson = Gson()
    var TAG = "ClientProductsDetail"

    var imageSilder: ImageSlider? = null
    var tvPname: TextView? = null
    var tvdescription: TextView? = null
    var tvPrice: TextView? = null
    var ivAdd: ImageView? = null
    var tvQuantity: TextView? = null
    var ivRemove: ImageView? = null
    var btnAgregar: Button? = null

    var counter = 1
    var productPrice = 0.0

    var sharedPref: SharedPref? = null
    var selectedProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_products_detail)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        sharedPref = SharedPref(this)

        imageSilder = findViewById(R.id.image_slider)
        tvPname = findViewById(R.id.tvPname)
        tvdescription = findViewById(R.id.tvdescription)
        tvPrice = findViewById(R.id.tvPrice)
        tvQuantity = findViewById(R.id.tvQuantity)
        ivAdd = findViewById(R.id.ivAdd)
        ivRemove = findViewById(R.id.ivRemove)
        btnAgregar = findViewById(R.id.btnAgregar)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))

        imageSilder?.setImageList(imageList)
        tvPname?.text = product?.name
        tvdescription?.text = product?.description
        tvPrice?.text = "$${product?.price}"

        ivAdd?.setOnClickListener {addItem()}
        ivRemove?.setOnClickListener {removeItem()}
        btnAgregar?.setOnClickListener {addToCart()}

        getProductsFromSharedPref()
    }

    private fun addToCart(){
        val index = getIndexOf(product?.id!!)

        if (index == -1){
            if (product?.quantity == 0){
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        }else{
            selectedProducts[index].quantity = counter
        }

        sharedPref?.save("order",selectedProducts)
        Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_LONG).show()
    }

    private fun getProductsFromSharedPref(){
        if (!sharedPref?.getData("order").isNullOrBlank()){ //valida si existe una orden en sharedPref
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            val index = getIndexOf(product?.id!!)

            if (index != -1){
                product?.quantity = selectedProducts[index].quantity
                tvQuantity?.text = "${product?.quantity}"
                productPrice = product?.price!! * product?.quantity!!
                tvPrice?.text = "$${product?.price}"
                btnAgregar?.setText("Editar producto")
                btnAgregar?.setBackgroundColor(resources.getColor(R.color.black))
            }

            for (p in selectedProducts){
                Log.d(TAG, "Shared Pref: $p")
            }
        }
    }

    private fun getIndexOf(idProduct: String): Int{
        var pos = 0

        for (p in selectedProducts){
            if(p.id == idProduct){
                return pos
            }
            pos++
        }
        return -1
    }


    private fun  addItem(){
        counter++
        productPrice = product?.price!! * counter
        product?.quantity = counter
        tvQuantity?.text = "${product?.quantity}"
        tvPrice?.text = "$${productPrice}"
    }

    private fun removeItem(){
        if (counter > 1){
            counter--
            productPrice = product?.price!! * counter
            product?.quantity = counter
            tvQuantity?.text = "${product?.quantity}"
            tvPrice?.text = "$${productPrice}"
        }
    }

}