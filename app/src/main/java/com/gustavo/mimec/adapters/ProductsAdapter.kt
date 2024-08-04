package com.gustavo.mimec.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.home.ClientHomeActivity
import com.gustavo.mimec.activities.client.products.detail.ClientProductsDetailActivity
import com.gustavo.mimec.activities.delivery.home.DeliveryHomeActivity
import com.gustavo.mimec.activities.taller.home.TallerHomeActivity
import com.gustavo.mimec.models.Category
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.models.Rol
import com.gustavo.mimec.utils.SharedPref

class ProductsAdapter(val context: Activity, val products : ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]
        holder.textViewProduct.text = product.name
        holder.textViewPrice.text = "$${product.price}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)


        holder.itemView.setOnClickListener {goToDetail(product)}
    }

    private fun goToDetail(product: Product){
            val i=Intent(context, ClientProductsDetailActivity::class.java)
            i.putExtra("product", product.toJson())
            context.startActivity(i)
    }

    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewProduct: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init{
            textViewProduct= view.findViewById(R.id.tvNombreProducto)
            textViewPrice= view.findViewById(R.id.tvPrecio)
            imageViewProduct= view.findViewById(R.id.ivProducto)
        }

    }

}