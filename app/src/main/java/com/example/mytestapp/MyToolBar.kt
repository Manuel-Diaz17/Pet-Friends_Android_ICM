package com.example.mytestapp

import androidx.appcompat.app.AppCompatActivity

class MyToolBar {
    fun show (activities: AppCompatActivity, title: String,upButton:Boolean,enButton:Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar_main))
        activities.supportActionBar?.title = title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
        activities.supportActionBar?.setHomeButtonEnabled(enButton)
    }
}