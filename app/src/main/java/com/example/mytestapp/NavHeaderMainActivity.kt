package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*

class NavHeaderMainActivity : AppCompatActivity() {

    lateinit var handler: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header_main)

        handler = DBHelper(this)

        val userLoggedInCredentials = handler.selectUserLoggedIn()

        if (userLoggedInCredentials != null) {
            val user = handler.selectUserData(userLoggedInCredentials.email, userLoggedInCredentials.password)
            if (user != null) {
                nav_header_textView.text = user.name + " " + user.lastname

            }
        }
    }
}