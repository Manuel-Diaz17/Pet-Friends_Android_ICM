package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    lateinit var handler: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        MyToolBar().show(this,"Profile",true,true)

        val loginCredentials = intent.getSerializableExtra("userCredentials") as UserCredentials
        Log.d("login", loginCredentials.toString())

        handler = DBHelper(this)

        val user = handler.selectUserData(loginCredentials.email, loginCredentials.password)






    }
}