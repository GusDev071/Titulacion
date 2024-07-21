package com.gustavo.mimec.activities

import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.mimec.R
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var txtRegistro: TextView
var etCorreo: EditText? =null
var etContrasena: EditText? = null
var btnIniciarSesion: Button? = null
var usersProvider = UsersProvider()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        txtRegistro = findViewById(R.id.txtRegistro)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnIniciarSesion = findViewById(R.id.btnLogin)

        txtRegistro.setOnClickListener {Registro()}
        btnIniciarSesion?.setOnClickListener {Login()}
    }

    private fun Login(){
        val correo = etCorreo?.text.toString()
        val contrasena = etContrasena?.text.toString()

        if(validarLogin(correo, contrasena)){

            usersProvider.login(correo, contrasena)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity", "Response: ${response.body()}")

                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }


        Log.d("MainActivity", "Correo: $correo")
      //  Log.d("MainActivity", "Contraseña: $contrasena")
    }

    fun String.CorreoValido():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun validarLogin(correo:String, contraseña:String):Boolean {

        if (correo.isBlank()) {
            return false
        }

        if (contraseña.isBlank()) {
            return false
        }

        if (!correo.CorreoValido()){
            return false
        }

        return true
    }


    private fun Registro() {
        val registro = Intent(this, RegisterActivity::class.java)
        startActivity(registro)
        finish()
    }
}