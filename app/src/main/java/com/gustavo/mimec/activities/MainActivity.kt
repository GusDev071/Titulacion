package com.gustavo.mimec.activities

import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gustavo.mimec.R


private lateinit var txtRegistro: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        txtRegistro = findViewById(R.id.txtRegistro)

        txtRegistro.setOnClickListener {Registro()}
    }

    private fun Registro() {
        val registro = Intent(this, RegisterActivity::class.java)
        startActivity(registro)
        finish()
    }
}