package com.gustavo.mimec.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import com.gustavo.mimec.R
import android.content.Intent

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtInicio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        txtInicio = findViewById(R.id.txtInicio)

        txtInicio.setOnClickListener { Inicio() }
    }

    private fun Inicio() {
        val inicio = Intent(this, MainActivity::class.java)
        startActivity(inicio)
        finish()
    }
}