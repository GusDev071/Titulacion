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
import com.gustavo.mimec.activities.client.shopping_cart.ClientShoppingCartActivity
import com.gustavo.mimec.activities.delivery.home.DeliveryHomeActivity
import com.gustavo.mimec.activities.taller.home.TallerHomeActivity
import com.gustavo.mimec.models.Category
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.models.Rol
import com.gustavo.mimec.utils.SharedPref

class ShppingCartAdapter(val context: Activity, val products : ArrayList<Product>): RecyclerView.Adapter<ShppingCartAdapter.ShoppingCartViewHolder>(){

    val sharedPref = SharedPref(context)

    init {
        (context as ClientShoppingCartActivity).setTotal(getTotal())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_cart, parent, false)
        return ShoppingCartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val product = products[position]
        holder.textViewProduct.text = product.name
        holder.textViewCounter.text = "${product.quantity}"
        holder.textViewPrice.text = "$${product.price * product.quantity!!}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        holder.imageViewAdd.setOnClickListener {addItem(product, holder)}
        holder.imageViewRemove.setOnClickListener {removeItem(product, holder)}
        holder.imageViewDelete.setOnClickListener {deleteItem(position)}
        //holder.itemView.setOnClickListener {goToDetail(product)}
    }

    private fun getTotal(): Double{
        var total = 0.0
        for (p in products){
            total = total + (p.quantity!! * p.price)
        }
        return total
    }

    private fun getIndexOf(idProduct: String): Int{
        var pos = 0

        for (p in products){
            if(p.id == idProduct){
                return pos
            }
            pos++
        }
        return -1
    }

    private fun deleteItem(position: Int){
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)
        sharedPref.save("order",products)
        (context as ClientShoppingCartActivity).setTotal(getTotal())
    }

    private fun  addItem(product: Product, holder: ShoppingCartViewHolder){

        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
        products[index].quantity = product.quantity

        holder.textViewCounter.text = "${product.quantity}"
        holder.textViewPrice.text = "$${product.quantity!! * product.price}"

        sharedPref.save("order",products)
        (context as ClientShoppingCartActivity).setTotal(getTotal())
    }

    private fun removeItem(product: Product, holder: ShoppingCartViewHolder){
        if (product.quantity!! > 1){
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity

            holder.textViewCounter.text = "${product.quantity}"
            holder.textViewPrice.text = "$${product.quantity!! * product.price}"

            sharedPref.save("order",products)
            (context as ClientShoppingCartActivity).setTotal(getTotal())
        }
    }

    private fun goToDetail(product: Product){
            val i=Intent(context, ClientProductsDetailActivity::class.java)
            i.putExtra("product", product.toJson())
            context.startActivity(i)
    }

    class ShoppingCartViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewProduct: TextView
        val textViewPrice: TextView
        val textViewCounter: TextView
        val imageViewProduct: ImageView
        val imageViewAdd: ImageView
        val imageViewRemove: ImageView
        val imageViewDelete: ImageView

        init{
            textViewProduct= view.findViewById(R.id.tv_product_name)
            textViewPrice= view.findViewById(R.id.tvPrice2)
            imageViewProduct= view.findViewById(R.id.iv_product)
            textViewCounter= view.findViewById(R.id.tvQuantity2)
            imageViewAdd= view.findViewById(R.id.ivAdd2)
            imageViewRemove= view.findViewById(R.id.ivRemove2)
            imageViewDelete= view.findViewById(R.id.ivDelete)
        }

    }

}