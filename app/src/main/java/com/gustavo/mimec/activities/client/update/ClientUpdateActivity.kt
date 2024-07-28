package com.gustavo.mimec.activities.client.update

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.usersProvider
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.UsersProvider
import com.gustavo.mimec.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {

    var circleImageUser: CircleImageView? = null
    var etNombre: EditText? = null
    var etApellidos: EditText? = null
    var etTelefono: EditText? = null
    var btnUpdate: Button? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    private  var imageFile: File? = null
    var usersProvider: UsersProvider? = null
    val TAG = "ClientUpdateActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_update)

        sharedPref = SharedPref(this)

        circleImageUser = findViewById(R.id.circleimage_user)
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etTelefono = findViewById(R.id.etTelefono)
        btnUpdate = findViewById(R.id.btnUpdate)

        getUserUserFromSession()

        usersProvider = UsersProvider(user?.SessionToken)

        etNombre?.setText(user?.name)
        etApellidos?.setText(user?.lastname)
        etTelefono?.setText(user?.phone)

        if (!user?.image.isNullOrBlank()) {
            Glide.with(this)
                .load(user?.image)
                .into(circleImageUser!!)
        }

        circleImageUser?.setOnClickListener{selectImage()}
        btnUpdate?.setOnClickListener{updateData()}
    }

    private fun updateData(){

        val nombre = etNombre?.text.toString()
        val apellidos = etApellidos?.text.toString()
        val telefono = etTelefono?.text.toString()

        user?.name = nombre
        user?.lastname = apellidos
        user?.phone = telefono

        if(imageFile!=null){
            usersProvider?.update(imageFile!! , user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.e(TAG, "Response: $response")
                    Log.e(TAG, "BODY: ${response.body()}")

                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.e(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            usersProvider?.updateWithoutImage(user!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.e(TAG, "Response: $response")
                    Log.e(TAG, "BODY: ${response.body()}")

                    Toast.makeText(this@ClientUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                    if (response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.e(TAG, "Error: ${t.message}")
                    Toast.makeText(this@ClientUpdateActivity, "Error: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })

        }

    }

    private fun saveUserInSession(data:String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

    private fun getUserUserFromSession(){
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)// Imagen que se almacena en el servidor
            circleImageUser?.setImageURI(fileUri)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
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