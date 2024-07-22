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
import com.gustavo.mimec.models.Rol
import com.gustavo.mimec.utils.SharedPref

class RolesAdapter(val context: Activity,val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>(){

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]
        holder.textViewRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageViewRol)


        holder.itemView.setOnClickListener {goToRol(rol)}
    }

    private fun goToRol(rol:Rol){
        if (rol.name == "TALLER"){

            sharedPref.save("rol","TALLER")

            val i=Intent(context, TallerHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "CLIENTE"){

            sharedPref.save("rol","CLIENTE")

            val i=Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "MECANICO"){

            sharedPref.save("rol","MECANICO")
            val i=Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }
    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textViewRol: TextView
        val imageViewRol: ImageView

        init{
            textViewRol= view.findViewById(R.id.textview_rol)
            imageViewRol= view.findViewById(R.id.imageview_rol)
        }

    }

}