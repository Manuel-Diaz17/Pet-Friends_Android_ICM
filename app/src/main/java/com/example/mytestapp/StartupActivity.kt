package com.example.mytestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class StartupActivity : AppCompatActivity() {
    //private lateinit var drawer: DrawerLayout
    //private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        MyToolBar().show(this,"Welcome to Pet Friends",false,true)

        //val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        val loginbutton : Button = findViewById(R.id.login_button)

        loginbutton.setOnClickListener {
            val intent: Intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }




}