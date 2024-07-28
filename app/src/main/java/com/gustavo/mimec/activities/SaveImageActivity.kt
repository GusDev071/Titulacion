package com.gustavo.mimec.activities

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.home.ClientHomeActivity
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.UsersProvider
import com.gustavo.mimec.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {

    val TAG = "SaveImageActivity"

    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null

    private  var imageFile: File? = null

    var usersProvider: UsersProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)

        getUserUserFromSession()

        usersProvider = UsersProvider(user?.SessionToken)

        circleImageUser = findViewById(R.id.circleImageUser)
        buttonNext = findViewById(R.id.btnNext)
        buttonConfirm = findViewById(R.id.btnConfirm)


        circleImageUser?.setOnClickListener {selectImage()}
        buttonNext?.setOnClickListener {goToClientHome()}
        buttonConfirm?.setOnClickListener {saveImage()}
    }

    private fun saveImage(){
        if(imageFile != null && user != null){
            usersProvider?.update(imageFile!! , user!!)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.e(TAG, "Response: $response")
                    Log.e(TAG, "BODY: ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.e(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
        else{
            Toast.makeText(this, "La imagen no puede ser nula ni los datos", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInSession(data:String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)

        goToClientHome()
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
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