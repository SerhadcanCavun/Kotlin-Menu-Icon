package com.example.project1

import IconAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }

        recyclerView = findViewById(R.id.rv)

        val iconList = listOf(
            IconItem(R.drawable.icon_logo, "WIFI"),
            IconItem(R.drawable.icon_logo2, "VOLUME"),
            IconItem(R.drawable.icon_logo3, "BLUETOOTH"),
            IconItem(R.drawable.icon_logo4, "ROTATE"),
            IconItem(R.drawable.icon_logo5, "PLANE MOD"),
            IconItem(R.drawable.icon_logo6, "LIGHT"),
            IconItem(R.drawable.icon_logo7, "POWER MODE"),
            IconItem(R.drawable.icon_logo8, "CELLULAR"),
            IconItem(R.drawable.icon_logo9, "BLUE LIGHT FILTER"),
        )

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = IconAdapter(iconList, this)
    }
}