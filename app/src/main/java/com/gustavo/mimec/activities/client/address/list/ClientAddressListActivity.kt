package com.gustavo.mimec.activities.client.address.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gustavo.mimec.R
import com.gustavo.mimec.activities.client.address.create.ClientAddressCreateActivity

class ClientAddressListActivity : AppCompatActivity() {

    var fabCreateAddress: FloatingActionButton? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_address_list)

        fabCreateAddress = findViewById(R.id.fabAdd)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(resources.getColor(R.color.black))
        toolbar?.title = "Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fabCreateAddress?.setOnClickListener { goToCreateAddress() }

    }

    private fun goToCreateAddress() {
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }
}