package com.gustavo.mimec.activities


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import com.gustavo.mimec.R
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gustavo.mimec.models.ResponseHttp
import com.gustavo.mimec.models.User
import com.gustavo.mimec.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"
    private lateinit var txtInicio: TextView
    var etNombre: EditText? = null
    var etApellidos: EditText? = null
    var etCorreo: EditText? = null
    var etTelefono: EditText? = null
    var etContrasena: EditText? = null
    var etConfirmarContrasena: EditText? = null
    var btnRegistrarse: Button? = null
    var usersProvider = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        txtInicio = findViewById(R.id.txtInicio)
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etContrasena = findViewById(R.id.etContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)

        txtInicio?.setOnClickListener { Inicio() }
        btnRegistrarse?.setOnClickListener { registro() }
    }

    private fun registro(){
        val nombre = etNombre?.text.toString()
        val apellidos = etApellidos?.text.toString()
        val correo = etCorreo?.text.toString()
        val telefono = etTelefono?.text.toString()
        val contrasena = etContrasena?.text.toString()
        val confirmarContrasena = etConfirmarContrasena?.text.toString()



        if(validarRegistro(nombre, apellidos, correo, telefono, contrasena, confirmarContrasena)){

            val user = User(
                name = nombre,
                latsname = apellidos,
                email = correo,
                phone = telefono,
                password = contrasena
            )

            usersProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error: ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

    fun String.CorreoValido():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun validarRegistro(
        nombre:String,
        apellidos:String,
        correo:String,
        telefono:String,
        contraseña:String,
        confirmarContrasena:String
    ):Boolean {

        if (nombre.isBlank()) {
            Toast.makeText(this, "Nombre vacio, debes ingresar tu nombre.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (apellidos.isBlank()) {
            Toast.makeText(this, "Apellidos vacio, debes ingresar tus apellidos.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (correo.isBlank()) {
            Toast.makeText(this, "Correo vacio, debes ingresar tu correo.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (telefono.isBlank()) {
            Toast.makeText(this, "Telefono vacio, debes ingresar tu telefono.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (contraseña.isBlank()) {
            Toast.makeText(this, "Contraseña vacia, debes ingresar tu contraseña.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmarContrasena.isBlank()) {
            Toast.makeText(this, "Debes confirmar tu contraseña.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!correo.CorreoValido()){
            Toast.makeText(this, "Correo invalido, debes ingresar un correo valido.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(contraseña != confirmarContrasena){
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun Inicio() {
        val inicio = Intent(this, MainActivity::class.java)
        startActivity(inicio)
        finish()
    }
}