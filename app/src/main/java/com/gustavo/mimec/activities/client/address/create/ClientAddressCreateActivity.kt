package com.gustavo.mimec.activities.client.address.create

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.address.map.ClientAddressMapActivity

class ClientAddressCreateActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var refPoint: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_address_create)

        toolbar = findViewById(R.id.toolbar)
        refPoint = findViewById(R.id.etRefPoint)

        toolbar?.setTitleTextColor(resources.getColor(R.color.black))
        toolbar?.title = "Nueva dirección"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        refPoint?.setOnClickListener{goToAddressMap()}
    }

    private fun goToAddressMap(){
        val i = Intent(this, ClientAddressMapActivity::class.java)
        startActivity(i)
    }
}