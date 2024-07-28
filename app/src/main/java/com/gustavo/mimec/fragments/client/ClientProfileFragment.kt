package com.gustavo.mimec.fragments.client

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.MainActivity
import com.gustavo.mimec.activities.SelectRolesActivity
import com.gustavo.mimec.models.User
import com.gustavo.mimec.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageUser: CircleImageView? = null
    var txtName: TextView? = null
    var txtEmail: TextView? = null
    var txtPhone: TextView? = null
    var sharedPref: SharedPref? = null
    var user: User? = null
    var imageViewLogout: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)
        sharedPref = SharedPref(requireActivity())

        buttonSelectRol = myView?.findViewById(R.id.btnSelectRole)
        buttonUpdateProfile= myView?.findViewById(R.id.btnUpdateProfile)
        circleImageUser = myView?.findViewById(R.id.circleimage_user)
        txtName = myView?.findViewById(R.id.txtName)
        txtEmail = myView?.findViewById(R.id.txtEmail)
        txtPhone = myView?.findViewById(R.id.txtPhone)
        imageViewLogout = myView?.findViewById(R.id.btnLogout)

        buttonSelectRol?.setOnClickListener{goToSelectRol()}
        imageViewLogout?.setOnClickListener{logout()}

        getUserUserFromSession()

        txtName?.text = "${user?.name} ${user?.lastname}"
        txtEmail?.text = user?.email
        txtPhone?.text = user?.phone

        if (!user?.image.isNullOrBlank()) {
            Glide.with(requireContext())
                .load(user?.image)
                .into(circleImageUser!!)
        }

        return myView
    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i =Intent (requireContext(), MainActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}