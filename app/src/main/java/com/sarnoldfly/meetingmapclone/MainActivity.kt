package com.sarnoldfly.meetingmapclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity(), OnButtonClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDessert = findViewById<Button>(R.id.btnDesierto)
        val btnRenega = findViewById<Button>(R.id.btnRenega)
        val btnTerreno = findViewById<Button>(R.id.btnTerreno)

        btnDessert.setOnClickListener { onButtonClick(1) }
        btnRenega.setOnClickListener { onButtonClick(2) }
        btnTerreno.setOnClickListener { onButtonClick(3) }



        val fragment = Fragment01()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onButtonClick(nicoClick: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as Fragment01
        fragment.traceRoute(nicoClick)
// el override tiene que estar dentro de la clase. creamos y sobrescribimos la funcion.
    }
}
interface OnButtonClickListener {
    fun onButtonClick(valor:Int)
}

