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


private lateinit var txtRegistro: TextView
private lateinit var etCorreo: EditText
private lateinit var etContrasena: EditText
private lateinit var btnIniciarSesion: Button

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
        btnIniciarSesion.setOnClickListener {Login()}
    }

    private fun Login(){
        val correo = etCorreo.text.toString()
        val contrasena = etContrasena.text.toString()

        if(validarLogin(correo, contrasena)){
            Toast.makeText(this, "Datos correctos", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }


        Log.d("MainActivity", "Correo: $correo")
        Log.d("MainActivity", "Contraseña: $contrasena")
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