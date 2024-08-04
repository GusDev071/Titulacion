package com.gustavo.mimec.fragments.taller

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.models.Category
import com.gustavo.mimec.models.Product
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.CategoriesProvider
import com.gustavo.mimec.providers.ProductsProvider
import com.gustavo.mimec.utils.SharedPref
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File




class TallerProductFragment : Fragment() {

    var myView: View? = null
    var etProducto: EditText? = null
    var etDescription: EditText? = null
    var etPrice: EditText? = null
    var ivImage1: ImageView? = null
    var ivImage2: ImageView? = null
    var ivImage3: ImageView? = null
    var btnCrear: Button? = null
    var imageFile1: File? = null
    var imageFile2: File? = null
    var imageFile3: File? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var categoriesProvider: CategoriesProvider? = null
    var TAG = "TallerProductFragment"
    var spCategory: Spinner? = null
    var idCategory = ""
    var productsProvider: ProductsProvider? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_taller_product, container, false)
        etProducto = myView?.findViewById(R.id.etProduct)
        etDescription = myView?.findViewById(R.id.etDescription)
        etPrice = myView?.findViewById(R.id.etPrice)
        ivImage1 = myView?.findViewById(R.id.ivImage1)
        ivImage2 = myView?.findViewById(R.id.ivImage2)
        ivImage3 = myView?.findViewById(R.id.ivImage3)
        btnCrear = myView?.findViewById(R.id.btnCrearProducto)
        spCategory = myView?.findViewById(R.id.spCategory)

        sharedPref = SharedPref(requireActivity())
        getUserUserFromSession()
        categoriesProvider = CategoriesProvider(user?.SessionToken!!)
        productsProvider = ProductsProvider(user?.SessionToken!!)
        getCategories()


        btnCrear?.setOnClickListener {createProduct()}
        ivImage1?.setOnClickListener {selectImage(101)}
        ivImage2?.setOnClickListener {selectImage(102)}
        ivImage3?.setOnClickListener {selectImage(103)}



        return myView
    }

    private fun createProduct() {
        val product = etProducto?.text.toString()
        val description = etDescription?.text.toString()
        val price = etPrice?.text.toString()
        val files = arrayListOf<File>()
        val dialog: AlertDialog = SpotsDialog.Builder().setContext(requireActivity()).setMessage("Espere un momento").build()


        if (isValidForm(product, description, price)){

            val product = Product(
                name = product,
                description = description,
                price = price.toDouble(),
                idCategory = idCategory
            )

            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)




            dialog.show()
            productsProvider?.create(files, product)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    dialog.dismiss()

                    Log.d(TAG, "Reponse: $response")
                    Log.d(TAG, "Body: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()

                    if(response.body()?.isSuccess == true){
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    dialog.dismiss()
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun resetForm(){
        etProducto?.setText("")
        etDescription?.setText("")
        etPrice?.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        ivImage1?.setImageResource(R.drawable.ic_image)
        ivImage2?.setImageResource(R.drawable.ic_image)
        ivImage3?.setImageResource(R.drawable.ic_image)

    }

    private fun isValidForm(name: String, description:String, price: String):Boolean {
        if (name.isNullOrBlank()) {
            Toast.makeText(requireContext(), "El nombre del producto es requerido", Toast.LENGTH_LONG).show()
            return false
        }
        if (description.isNullOrBlank()) {
            Toast.makeText(requireContext(), "La descripción del producto es requerida", Toast.LENGTH_LONG).show()
            return false
        }
        if (price.isNullOrBlank()) {
            Toast.makeText(requireContext(), "El precio del producto es requerido", Toast.LENGTH_LONG).show()
            return false
        }
        if(ivImage1 == null || ivImage2 == null || ivImage3 == null){
            Toast.makeText(requireContext(), "Las 3 imagenes son requeridas", Toast.LENGTH_LONG).show()
            return false
        }
        if (idCategory.isNullOrBlank()){
            Toast.makeText(requireContext(), "La categoria es requerida", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            if (requestCode == 101) {
                imageFile1 = File(fileUri?.path)// Imagen que se almacena en el servidor
                ivImage1?.setImageURI(fileUri)
            }else if (requestCode == 102) {
                imageFile2 = File(fileUri?.path)// Imagen que se almacena en el servidor
                ivImage2?.setImageURI(fileUri)
            }else if (requestCode == 103) {
                imageFile3 = File(fileUri?.path)// Imagen que se almacena en el servidor
                ivImage3?.setImageURI(fileUri)
            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(requireContext(), "Tarea se cancelo", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null){
                    categories = response.body()!!

                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(),android.R.layout.simple_dropdown_item_1line, categories)
                    spCategory?.adapter = arrayAdapter
                    spCategory?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
                            idCategory = categories[position].id!!
                            Log.d(TAG, "ID: $idCategory")
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(requestCode)
    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}