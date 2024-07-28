package com.gustavo.mimec.fragments.taller

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.models.Category
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.CategoriesProvider
import com.gustavo.mimec.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TallerCategoryFragment : Fragment() {

    var myView: View? = null
    var ivCategory: ImageView? = null
    var etCategory: EditText? = null
    var btnCreateCategory: Button? = null
    private  var imageFile: File? = null
    var categoriesProvider: CategoriesProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null
    var TAG = "TallerCategoryFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_taller_category, container, false)

        ivCategory = myView?.findViewById(R.id.ivCategory)
        etCategory = myView?.findViewById(R.id.etCategory)
        btnCreateCategory = myView?.findViewById(R.id.btnCreateCategory)

        sharedPref = SharedPref(requireActivity())

        ivCategory?.setOnClickListener {selectImage()}
        btnCreateCategory?.setOnClickListener{createCategory()}

        getUserUserFromSession()
        categoriesProvider = CategoriesProvider(user?.SessionToken!!)

        return myView
    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private  fun createCategory() {
        val name = etCategory?.text.toString()
        if(imageFile !=null){

            val category = Category(name = name)

            categoriesProvider?.create(imageFile!! , category)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.e(TAG, "Response: $response")
                    Log.e(TAG, "BODY: ${response.body()}")

                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()

                    if(response.body()?.isSuccess == true){
                        clearForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.e(TAG, "Error: ${t.message}")
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            Toast.makeText(requireContext(), "Seleccione una imagen", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearForm(){
        etCategory?.setText("")
        ivCategory?.setImageResource(R.drawable.ic_image)
        imageFile = null
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)// Imagen que se almacena en el servidor
            ivCategory?.setImageURI(fileUri)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

}