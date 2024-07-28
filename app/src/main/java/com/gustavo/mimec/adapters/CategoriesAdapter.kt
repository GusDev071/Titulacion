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
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.home.ClientHomeActivity
import com.gustavo.mimec.activities.delivery.home.DeliveryHomeActivity
import com.gustavo.mimec.activities.taller.home.TallerHomeActivity
import com.gustavo.mimec.models.Category
import com.gustavo.mimec.models.Rol
import com.gustavo.mimec.utils.SharedPref

class CategoriesAdapter(val context: Activity, val categories : ArrayList<Category>): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]
        holder.textViewCategory.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)


        /*holder.itemView.setOnClickListener {goToRol(rol)}*/
    }

    /*private fun goToRol(rol:Rol){
            val i=Intent(context, TallerHomeActivity::class.java)
            context.startActivity(i)
    }*/

    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init{
            textViewCategory= view.findViewById(R.id.TxtCategory)
            imageViewCategory= view.findViewById(R.id.imageViewCategory)
        }

    }

}